package unam.fi.ldp.videos.Activitys
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import unam.fi.ldp.videos.R

import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.MoreVideoModel.User
import unam.fi.ldp.videos.Resources.CategoryAdapter
import unam.fi.ldp.videos.Resources.Image



class MainActivity : AppCompatActivity() {

    private val manageDB = ManageMoreVideoDB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        loadUser()

        categoryList.layoutManager = LinearLayoutManager(this)
        categoryList.adapter = CategoryAdapter(this)

        user_img_ex.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            val bundle : Bundle = Bundle()
            bundle.putSerializable("user",loadUser())
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        })

    }

    private fun loadUser(): User? {
        val prefs = getSharedPreferences("MoreVideos", Context.MODE_PRIVATE)
        var user : User ? = null
        if(prefs.getString("idUser",null)!=null){
            user = manageDB.getUser(MoreVideo.UsersHeaders.ID+"=?",arrayOf(prefs.getString("idUser",null)))
            Image(this).loadImageUser(
                    user!!.img,
                    user_img_ex)

        }
        return user
    }

    override fun onResume() {
        super.onResume()
        loadUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if(loadUser()!!.type==0){
            inflater.inflate(R.menu.main, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, RootActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
