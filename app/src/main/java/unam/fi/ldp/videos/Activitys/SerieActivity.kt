package unam.fi.ldp.videos.Activitys
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import kotlinx.android.synthetic.main.activity_serie.*
import kotlinx.android.synthetic.main.app_bar_serie.*
import kotlinx.android.synthetic.main.content_serie.*
import kotlinx.android.synthetic.main.nav_content.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideoModel.Comment
import unam.fi.ldp.videos.Database.MoreVideoModel.Season
import unam.fi.ldp.videos.Database.MoreVideoModel.Serie
import unam.fi.ldp.videos.Database.MoreVideoModel.User
import unam.fi.ldp.videos.R
import unam.fi.ldp.videos.Resources.ChaptersAdapter
import unam.fi.ldp.videos.Resources.CommentsAdapter
import unam.fi.ldp.videos.Resources.SeasonAdapter
import android.support.v7.widget.DividerItemDecoration
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Resources.Image
import java.util.Date


class SerieActivity : AppCompatActivity() {

    private var manageMoreVideoDB :ManageMoreVideoDB = ManageMoreVideoDB(this)

    private var serie: Serie? = null


    private var adapterSeason : SeasonAdapter? = null
    private var adapterComments: CommentsAdapter? = null

    private var idSeason:String? = null
    private var like:Int? = null
    private var dislike:Int? = null
    private var user: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_serie)
        setSupportActionBar(toolbarSerie)

        try {
            val bundle : Bundle = intent.extras.getBundle("bundle")
            serie  =   bundle.getSerializable("serie") as Serie

            user = loadUser()
            collapsing_toolbar_layout.title = serie!!.title
            collapsing_toolbar_layout.setOnClickListener({activityDetail()})

            Image(this).loadImageUser(user!!.img,user_img_ex_s)
            Image(this).loadImageUser(user!!.img,nav_view.getHeaderView(0).nav_img_user)

            if(user!!.lastname == null) {
                nav_view.getHeaderView(0).nav_header_title.text =   user!!.name
            }else{
                nav_view.getHeaderView(0).nav_header_title.text = user!!.name + " " + user!!.lastname
            }
            nav_view.getHeaderView(0).nav_header_subtitle.text= user!!.email
            nav_view.getHeaderView(0).nav_img_user.setOnClickListener({ activityUser() })

            user_img_ex_s.setOnClickListener({ activityUser() })


            Image(this).loadImageSerie(serie!!.img,holder_image)

            viewSeason()
            viewComments()
        }catch (e :NullPointerException){
            activityDetail()
        }

    }

    private fun activityDetail() {
        val intent = Intent(this, DetailSerieActivity::class.java)
        val bundle : Bundle = Bundle()
        bundle.putSerializable("serie",serie)
        intent.putExtra("bundle",bundle)
        startActivity(intent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        viewSeason()
    }


    private fun loadUser(): User? {
        val prefs = getSharedPreferences("MoreVideos", Context.MODE_PRIVATE)
        var user : User ? = null
        if(prefs.getString("idUser",null)!=null){
            user = manageMoreVideoDB.getUser(MoreVideo.UsersHeaders.ID+"=?",arrayOf(prefs.getString("idUser",null)))
        }
        return user
    }

    private fun activityUser(){
        val intent = Intent(this, UserActivity::class.java)
        val bundle : Bundle = Bundle()
        bundle.putSerializable("user",loadUser())
        intent.putExtra("bundle",bundle)
        startActivity(intent)
    }

    private fun viewSeason() = try {

        adapterSeason = SeasonAdapter(this,  manageMoreVideoDB.getSeasons(serie!!.id))
        spinner.adapter = adapterSeason
        spinner.setSelection(adapterSeason!!.count-1)
        idSeason = adapterSeason!!.getItem(adapterSeason!!.count-1)!!.id

        updatePunctuation()


        like_season.setOnClickListener({
            val punt : Int? = manageMoreVideoDB.getSeasonPunctuationUser(user!!.id, idSeason!!)
            if( punt ==null){
                manageMoreVideoDB.insertSeasonPunctuation(1,user!!.id, idSeason!!)
            }else if(punt != 1){
                manageMoreVideoDB.updateSeasonPunctuationUser(1,user!!.id, idSeason!!)
            }else{
                manageMoreVideoDB.deleteSeasonPunctuation(user!!.id, idSeason!!)
            }
            updatePunctuation()
        })
        dislike_season.setOnClickListener({
            val punt : Int? = manageMoreVideoDB.getSeasonPunctuationUser(user!!.id, idSeason!!)
            if( punt ==null){
                manageMoreVideoDB.insertSeasonPunctuation(0,user!!.id, idSeason!!)
            }else if(punt != 0){
                manageMoreVideoDB.updateSeasonPunctuationUser(0,user!!.id, idSeason!!)
            }else{
                manageMoreVideoDB.deleteSeasonPunctuation(user!!.id, idSeason!!)
            }
            updatePunctuation()
        })

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                RecyclerViewChapters.adapter = ChaptersAdapter(this@SerieActivity,adapterSeason!!.getItem(pos)!!.id,user!!,adapterSeason!!.getItem(pos)!!,serie!!)
                RecyclerViewChapters.adapter.notifyDataSetChanged()
                RecyclerViewChapters.refreshDrawableState()
                Image(baseContext).loadPictureHolder(adapterSeason!!.getItem(pos)!!.img,holder_image)
                idSeason = adapterSeason!!.getItem(pos)!!.id

                RecyclerViewChapters.removeAllViews()
                adapterComments = CommentsAdapter(baseContext, idSeason!!,user!!)
                RecyclerViewComments.adapter = adapterComments
                RecyclerViewChapters.adapter.notifyDataSetChanged()
                updatePunctuation()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }

        }
            RecyclerViewChapters.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            RecyclerViewChapters.adapter = ChaptersAdapter(this, adapterSeason!!.getItem(adapterSeason!!.count-1)!!.id,user!!,adapterSeason!!.getItem(adapterSeason!!.count-1)!!, serie!!)
        }catch (e: IndexOutOfBoundsException){
        this.finish()
    }catch (e: KotlinNullPointerException){
        this.finish()
    }

    private fun updatePunctuation() {
        var punctuation = manageMoreVideoDB.getSeasonPunctuation(idSeason!!)
        like = punctuation.first
        dislike = punctuation.second
        like_season.text = like.toString()
        dislike_season.text = dislike.toString()
    }


    private fun viewComments() {
        adapterComments = CommentsAdapter(this,  adapterSeason!!.getItem(adapterSeason!!.count-1)!!.id,user!!)

        RecyclerViewComments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RecyclerViewComments.adapter= adapterComments

        val verticalDecoration = DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(this, R.drawable.divider_comment)
        verticalDecoration.setDrawable(verticalDivider!!)
        RecyclerViewComments.addItemDecoration(verticalDecoration)

        nav_view.getHeaderView(0).nav_comment.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, _ ->
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        val idComment :String = MoreVideo.IdCommnet.generateCommentID()
                        manageMoreVideoDB.insertComment(Comment(idComment,v.text.toString(), Date(),user!!.id))
                        manageMoreVideoDB.insertSeasonComment(idComment,this.idSeason!!)
                        adapterComments = CommentsAdapter(this, this.idSeason!!,user!!)
                        RecyclerViewChapters.removeAllViews()
                        RecyclerViewComments.adapter = adapterComments
                        RecyclerViewComments.adapter.notifyDataSetChanged()
                        v.text=""
                        return@OnEditorActionListener true
                    }
                    false
                })



    }


}

