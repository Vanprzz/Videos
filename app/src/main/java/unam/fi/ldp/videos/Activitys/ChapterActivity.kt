package unam.fi.ldp.videos.Activitys

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import unam.fi.ldp.videos.R

import kotlinx.android.synthetic.main.activity_chapter.*
import kotlinx.android.synthetic.main.app_bar_chapter.*
import kotlinx.android.synthetic.main.content_chapter.*
import kotlinx.android.synthetic.main.nav_content.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.MoreVideoModel.*
import unam.fi.ldp.videos.Resources.CommentsAdapter
import unam.fi.ldp.videos.Resources.Image
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast
import kotlinx.android.synthetic.main.app_bar_serie.*

/**
 * PEREZ URIBE CESAR IVAN
 * */

class ChapterActivity : AppCompatActivity() {

    private var manageMoreVideoDB : ManageMoreVideoDB = ManageMoreVideoDB(this)
    private var mediaController: MediaController? = null

    private var serie :Serie ?=null
    private var season : Season?=null
    private var chapter :Chapter ?=null
    private var user :User?=null

    private var adapterComments: CommentsAdapter? = null

    private var like:Int? = null
    private var dislike:Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_chapter)

        try {
            val bundle : Bundle = intent.extras.getBundle("bundle")
            serie  =   bundle.getSerializable("serie") as Serie
            season  =   bundle.getSerializable("season") as Season
            chapter  =   bundle.getSerializable("chapter") as Chapter
            user  =   bundle.getSerializable("user") as User

            Image(this).loadImageUser(user!!.img,user_img_ex_chapter)
            Image(this).loadImageUser(user!!.img,nav_view_c.getHeaderView(0).nav_img_user)

            user_img_ex_chapter.setOnClickListener({activityUser()})
            nav_view_c.getHeaderView(0).setOnClickListener({activityUser()})

            toolbarChapter.title = serie!!.title
            setSupportActionBar(toolbarChapter)


            if(user!!.lastname == null) {
                nav_view_c.getHeaderView(0).nav_header_title.text =  user!!.name
            }else{
                nav_view_c.getHeaderView(0).nav_header_title.text = user!!.name + " " + user!!.lastname
            }

            nav_view_c.getHeaderView(0).nav_header_subtitle.text = user!!.email
        }catch (e:NullPointerException){

        }

        viewComponents()
        viewComments()
    }



    private fun viewComponents() {
        Image(this).loadImageSerie(season!!.img,c_img_chapter)
        c_text_name.text = chapter!!.name
        c_text_price.text = resources.getText(R.string.prompt_price).toString()+chapter!!.price
        c_text_name_s.text = season!!.name
        c_text_duration.text = chapter!!.duration.toString()+" min"
        c_text_synopsis.text = "SYNOPSIS: \n"+chapter!!.synopsis

        val subtitles = manageMoreVideoDB.getSubtitle(chapter!!.id)
        val language: ArrayList<String> = ArrayList<String>()

        for(subtitle in subtitles){
            language.add(subtitle.language)
        }
        view_subtitle.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, language)
        view_subtitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(parent!!.context,
                        subtitles[position].author +"\n"+subtitles[position].language, Toast.LENGTH_SHORT).show()
            }

        }


        c_text_price.setOnClickListener({
            var builder = AlertDialog.Builder(this)

            builder.setMessage(this.resources.getString( R.string.prompt_messege_buy )+" of "+c_text_price.text)
            builder.setPositiveButton(R.string.prompt_ok,  DialogInterface.OnClickListener() { _: DialogInterface, i: Int ->
                val money = user!!.money

                if(money > chapter!!.price){

                    user!!.money = user!!.money - chapter!!.price
                    manageMoreVideoDB.updateUser(user!!)

                    if(user!!.lastname == null) {
                        nav_view_c.getHeaderView(0).nav_header_title.text = "$"+user!!.money.toString() +"\t\t" + user!!.name
                    }else{
                        nav_view_c.getHeaderView(0).nav_header_title.text = "$"+user!!.money.toString() +"\t\t" +user!!.name + " " + user!!.lastname
                    }

                    viewVideo()
                }else{
                    var builder = AlertDialog.Builder(this)
                    builder.setMessage(this.resources.getString( R.string.prompt_messege_buy_error )+" of "+c_text_price.text)
                    val dialog = builder.create()
                    dialog.show()
                }
            })
            builder.setNegativeButton(R.string.prompt_cancel, DialogInterface.OnClickListener() { _: DialogInterface, _: Int ->
                finish()
            })
            val dialog = builder.create()
            dialog.show()
        })
        updatePunctuation()

        like_chapter.setOnClickListener({
            val punt : Int? = manageMoreVideoDB.getChapterPunctuationUser(user!!.id, chapter!!.id)
            if( punt ==null){
                manageMoreVideoDB.insertChapterPunctuation(1,user!!.id, chapter!!.id)
            }else if(punt != 1){
                manageMoreVideoDB.updateChapterPunctuationUser(1,user!!.id, chapter!!.id)
            }else{
                manageMoreVideoDB.deleteChapterPunctuation(user!!.id, chapter!!.id)
            }
            updatePunctuation()
        })
        dislike_chapter.setOnClickListener({
            val punt : Int? = manageMoreVideoDB.getChapterPunctuationUser(user!!.id, chapter!!.id)
            if( punt ==null){
                manageMoreVideoDB.insertChapterPunctuation(0,user!!.id, chapter!!.id)
            }else if(punt != 0){
                manageMoreVideoDB.updateChapterPunctuationUser(0,user!!.id, chapter!!.id)
            }else{
                manageMoreVideoDB.deleteChapterPunctuation(user!!.id, chapter!!.id)
            }
            updatePunctuation()
        })


    }

    private fun viewVideo() {
        //videoChapter.setVideoPath(chapter!!.img)
        videoChapter.setVideoPath("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4")
        mediaController = MediaController(this)
        mediaController?.setAnchorView(videoChapter)

        videoChapter.setOnPreparedListener { mp ->
            mp.isLooping = true
            Log.i("VIDEO", "Duration = " + videoChapter.duration)
        }
        videoChapter.start()
    }

    private fun viewComments() {
        adapterComments = CommentsAdapter(this,  chapter!!.id, user!!)

        RecyclerViewComments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RecyclerViewComments.adapter= adapterComments

        val verticalDecoration = DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(this, R.drawable.divider_comment)
        verticalDecoration.setDrawable(verticalDivider!!)
        RecyclerViewComments.addItemDecoration(verticalDecoration)


        nav_view_c.getHeaderView(0).nav_comment.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                val idComment :String = MoreVideo.IdCommnet.generateCommentID();
                manageMoreVideoDB.insertComment(Comment(idComment,v.text.toString(), Date(),user!!.id))
                manageMoreVideoDB.insertChapterComment(idComment,chapter!!.id)

                adapterComments = CommentsAdapter(this, chapter!!.id,user!!)

                RecyclerViewComments.adapter = adapterComments
                RecyclerViewComments.adapter.notifyDataSetChanged()
                v.text=""
                return@OnEditorActionListener true
            }
            false
        })

    }


    private fun updatePunctuation() {
        var punctuation = manageMoreVideoDB.getChapterPunctuation(chapter!!.id)
        like = punctuation.first
        dislike = punctuation.second
        like_chapter.text = like.toString()
        dislike_chapter.text = dislike.toString()
    }

    private fun activityUser(){
        val intent = Intent(this, UserActivity::class.java)
        val bundle : Bundle = Bundle()
        bundle.putSerializable("user",user)
        intent.putExtra("bundle",bundle)
        startActivity(intent)
    }



}
