package unam.fi.ldp.videos.Activitys
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.MoreVideoModel
import unam.fi.ldp.videos.R
import unam.fi.ldp.videos.Resources.Image
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserActivity : AppCompatActivity() {

    private val REQUEST_TAKE_PHOTO = 1
    private val REQUEST_IMAGE_GET = 2

    private var mCurrentPhotoPath: String? = null

    private var user : MoreVideoModel.User?=null
    private var manageMoreVideoDB : ManageMoreVideoDB = ManageMoreVideoDB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        bdate.visibility = View.INVISIBLE
        form_signup.visibility = View.INVISIBLE
        register_button.text = resources.getText(R.string.prompt_save)
        try{
        val prefs = getSharedPreferences("MoreVideos", Context.MODE_PRIVATE)
        if(prefs.getString("idUser",null)!=null){

            val manageDB = ManageMoreVideoDB(this)
            user = manageDB.getUser(MoreVideo.UsersHeaders.ID+"=?",arrayOf(prefs.getString("idUser",null)))

            Image(this).loadImageUser(user!!.img,imageButton)
            name.setText(user!!.name)
            lastname.setText(user!!.lastname)
            money.text = resources.getText(R.string.prompt_money).toString()+"$"+ user!!.money
        }
        }catch (e:NullPointerException){
            finish()
        }

        log_out.setOnClickListener({
            val prefs = getSharedPreferences("MoreVideos", Context.MODE_PRIVATE)

            val editor = prefs.edit()
            editor.putString("idUser",null)
            editor.commit()
            finishAffinity()
        })

        register_button.setOnClickListener({
            user!!.name = name.text.toString()
            user!!.lastname = lastname.text.toString()
            manageMoreVideoDB.updateUser(this!!.user!!)
            finish()
        })


        imageButton.setOnClickListener {
            val permissionCheck:Int = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
            } else {
                selectPicture()
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic()

            var extras :Bundle= data!!.extras
            var imageBitmap: Bitmap = extras.get("data") as (Bitmap)
            user!!.img = mCurrentPhotoPath
            Image(this).loadImageUser(imageBitmap,imageButton)

        }else if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK){
            val fullPhotoUri:Uri =data!!.data
            user!!.img = getPath(this,fullPhotoUri)
            Image(this).loadImageUser(user!!.img,imageButton)

        }

    }

    private fun selectPicture()  {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setItems(R.array.media, DialogInterface.OnClickListener { _, which ->
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
            var photoFile: File?= null
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
        }catch (e : IOException){

        }
        return image

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
