package unam.fi.ldp.videos.Activitys
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.app.DatePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import java.text.SimpleDateFormat
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import unam.fi.ldp.videos.Resources.Image

import kotlinx.android.synthetic.main.activity_signup.*
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideoModel.User
import unam.fi.ldp.videos.R
import java.io.File
import java.io.IOException
import java.util.*
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    private val REQUEST_TAKE_PHOTO = 1
    private val REQUEST_IMAGE_GET = 2

    private var mCurrentPhotoPath: String? = null
    // Store values at the time of the login attempt.
    private var imgStr :String ?= null
    private var bDateStr :String ?= null
    private var nameStr :String ?= null
    private var lastNameStr :String ?= null
    private var emailStr :String ?= null
    private var passwordStr :String ?= null
    private var password2Str :String ?= null



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set up the login form.


        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptRegister()
                return@OnEditorActionListener true
            }
            false
        })

        register_button.setOnClickListener { attemptRegister() }
        bdate.setOnClickListener { attemptDate() }

        out.visibility=View.INVISIBLE

        imageButton.setOnClickListener {
            val permissionCheck:Int = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
            } else {
                selectPicture()
            }
        }
    }

    private fun selectPicture()  {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setItems(R.array.media,DialogInterface.OnClickListener { _, which ->
            if(which==0){
                    dispatchTakePictureIntent()
            }else{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET)
                }
            }

        }).show()
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent: Intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File ?= null
            try {
                photoFile = createImageFile()
            } catch (ie : IOException) {
                // Error occurred while creating the File
                Log.e("ERROR RRRR",ie.toString())
            }
            if (photoFile != null) {
                val photoURI : Uri = FileProvider.getUriForFile(this,
                "unam.fi.ldp.videos.fileprovider",
                photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile() : File? {
        var image: File? = null
        try {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName: String = "MoreVideo_" + timeStamp + "_"
            val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            )
            mCurrentPhotoPath = image.absolutePath
        }catch (e :IOException ){

        }
        return image

    }


     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic()

            var extras :Bundle= data!!.extras
            var imageBitmap: Bitmap= extras.get("data") as (Bitmap)
            imgStr = mCurrentPhotoPath
            Image(this).loadImageUser(imageBitmap,imageButton)

        }else if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK){
            val fullPhotoUri:Uri =data!!.data
            imgStr = getPath(this,fullPhotoUri)
            Image(this).loadImageUser(imgStr,imageButton)

        }
    }

    private fun attemptDate() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                bdate.text = SimpleDateFormat("dd/MM/yyyy").format(cal.time)
        }

        bdate.setOnClickListener {
                DatePickerDialog(this, dateSetListener,
                        1995,
                        Calendar.JANUARY,
                        1).show()
        }

    }


    private fun attemptRegister() {

         bDateStr = bdate.text.toString()
         nameStr = name.text.toString()
         lastNameStr = lastname.text.toString()
         emailStr = email.text.toString()
         passwordStr = password.text.toString()
         password2Str = password2.text.toString()


        // Reset errors.
        name.error = null
        lastname.error = null
        email.error = null
        password.error = null
        password2.error = null



        var cancel = false
        var focusView: View? = null


        if (!isBdateValid(bDateStr!!)) {
            bdate.text = getString(R.string.prompt_bdate)+" "+ getString(R.string.error_field_required)
            cancel = true
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(nameStr)) {
            name.error = getString(R.string.error_field_required)
            focusView = name
            cancel = true
        } else if (!isNameValid(nameStr!!)) {
            name.error = getString(R.string.error_invalid_name)
            focusView = name
            cancel = true
        }


        if (lastNameStr!=""){
            if(!isNameValid(lastNameStr)) {
                lastname.error = getString(R.string.error_invalid_name)
                focusView = lastname
                cancel = true
            }
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr!!)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }else if (!isPasswordValid(passwordStr!!)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password2Str) ) {
            password2.error = getString(R.string.error_field_required)
            focusView = password2
            cancel = true
        }else if (!password2Str.equals(passwordStr)) {
            password2.error = getString(R.string.error_incorrect_password)
            focusView = password2
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            //val manageMoreVideoDB = ManageMoreVideoDB(baseContext)
            val user : User =
                    User(MoreVideo.IdUser.generateUserID(), nameStr!!,lastNameStr!!,emailStr!!,passwordStr!!,Date(),imgStr,100,1)

            val manageMoreVideoDB = ManageMoreVideoDB(baseContext)
            if(manageMoreVideoDB.insertUser(user)){
                val prefs = getSharedPreferences("MoreVideos", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("idUser",user.id)
                editor.commit()

                finish()
            }else{
                Toast.makeText(this,R.string.prompt_error_sign,Toast.LENGTH_LONG).show()
            }
        }

    }



    private fun isBdateValid(bdate: String): Boolean {
        val pattern = Pattern.compile("^([0-2][0-9]|3[0-1])(\\/|/)(0[1-9]|1[0-2])\\2(\\d{4})\$")
        val matcher = pattern.matcher(bdate)

        return matcher.matches()
    }

    private fun isNameValid(name: String?): Boolean {
        val pattern = Pattern.compile("^(?:[A-Za-z]{2,15}\\b ?){1,3}\$")
        val matcher = pattern.matcher(name)

        return matcher.matches()
    }


    private fun isEmailValid(email: String): Boolean {
        //val pattern = Pattern.compile("^[\\w]+@{1}[\\w]+\\.+[a-z]{2,3}\$")
        val pattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})$")

        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    private fun isPasswordValid(password: String): Boolean {

        //val pattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\w+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$")
        val pattern = Pattern.compile("(?=^.{8,}\$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*\$")

        val matcher = pattern.matcher(password)

        return matcher.matches()
    }


    private fun getPath(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                val column_index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


}
