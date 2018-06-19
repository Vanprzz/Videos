package unam.fi.ldp.videos.Activitys
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.view.ActionMode.Callback
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.content_root.*
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.R
import unam.fi.ldp.videos.Resources.RootAdapter


class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setSupportActionBar(toolbarRoot)

        user_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        user_list.adapter = RootAdapter(this)
    }

}
