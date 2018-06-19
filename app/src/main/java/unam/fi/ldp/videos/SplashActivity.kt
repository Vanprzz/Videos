package unam.fi.ldp.videos
/**
 * PEREZ URIBE CESAR IVAN
 * */
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import unam.fi.ldp.videos.Activitys.LoginActivity
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import unam.fi.ldp.videos.Activitys.MainActivity


class SplashActivity : AppCompatActivity() {

    private val time = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val prefs = getSharedPreferences("MoreVideos", Context.MODE_PRIVATE)


        Handler().postDelayed(Runnable {
            if(prefs.getString("idUser",null)==null){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, time.toLong())
    }
}
