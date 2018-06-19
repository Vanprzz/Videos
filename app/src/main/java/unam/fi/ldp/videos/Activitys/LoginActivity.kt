package unam.fi.ldp.videos.Activitys
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView

import android.content.Context
import android.content.Intent

import kotlinx.android.synthetic.main.activity_login.*
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.MoreVideoModel.User
import unam.fi.ldp.videos.R
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin()}
        email_sign_up_button.setOnClickListener { attemptSignup() }
    }


    private fun attemptLogin() {
        // Reset errors.
        email.error = null
        password.error = null


        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if(!isPasswordValid(emailStr,passwordStr)){
            password.error = getString(R.string.error_incorrect_password)
            focusView = password
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            val intent = Intent(this, MainActivity::class.java).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }
    }

    private fun attemptSignup() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun isEmailValid(email: String): Boolean {
        //val pattern = Pattern.compile("^[\\w]+@{1}[\\w]+\\.+[a-z]{2,3}\$")
        val pattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})$")

        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    private fun isPasswordValid(email :String,password: String): Boolean {

        val manageDB = ManageMoreVideoDB(baseContext)
        val user: User? = manageDB.getUser(MoreVideo.UsersHeaders.EMAIL+"=? AND "+MoreVideo.UsersHeaders.PASSWORD+"=?",arrayOf(email,password ))

        if(user!=null){
            val prefs = getSharedPreferences("MoreVideos", Context.MODE_PRIVATE)

            val editor = prefs.edit()
            editor.putString("idUser",user.id)
            editor.commit()
            return true
        }
        return false
    }





}
