package unam.fi.ldp.videos.Resources
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.chapters_list.view.*
import unam.fi.ldp.videos.Activitys.ChapterActivity
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideoModel.Chapter
import unam.fi.ldp.videos.Database.MoreVideoModel.User
import unam.fi.ldp.videos.Database.MoreVideoModel.Serie
import unam.fi.ldp.videos.Database.MoreVideoModel.Season
import unam.fi.ldp.videos.R

class ChaptersAdapter(val context: Activity, id_Season: String?,val user : User,val season : Season,val serie : Serie ) : RecyclerView.Adapter<ChaptersAdapter.ChapterViewHolder>() {
    private val items = getChapters(id_Season)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapters_list, parent, false)
        return ChapterViewHolder(v)

    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        Image(this.context).loadImageSerie( items!![position].img,holder.mImg )
        holder.mName?.text = items[position].name
        holder.mSynopsis?.text = items[position].synopsis
        holder.mPrice?.text = "$"+items[position].price
        holder.mDuration?.text = items[position].duration.toString()+" min"
        holder.mCard?.setOnClickListener ({

            // User clicked OK button
            val intent = Intent(this.context, ChapterActivity::class.java)

            val bundle : Bundle = Bundle()
            bundle.putSerializable("serie",serie)
            bundle.putSerializable("season",season)
            bundle.putSerializable("chapter",items[position])
            bundle.putSerializable("user",user)



            intent.putExtra("bundle",bundle)
            startActivity(this.context,intent,null)
        }
        )
    }

    class ChapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val mImg = v.img_chapter
        val mName = v.text_name
        val mSynopsis = v.text_synopsis
        val mPrice = v.text_price
        val mDuration = v.text_duration
        val mCard = v.card_chapter
    }


    private fun getChapters(idSeason:String?): ArrayList<Chapter>? {
        if(idSeason!=null)
            return ManageMoreVideoDB(this.context).getChapter(idSeason)
        return null
    }

}