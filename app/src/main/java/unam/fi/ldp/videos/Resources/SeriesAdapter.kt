package unam.fi.ldp.videos.Resources
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.series_list.view.*
import unam.fi.ldp.videos.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import unam.fi.ldp.videos.Activitys.SerieActivity
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideoModel.Serie
import unam.fi.ldp.videos.Database.MoreVideoModel.User



class SeriesAdapter(var context: Context, id_Category : String)
    : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {

    private val items = ManageMoreVideoDB(this.context).getSeries(id_Category)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.series_list, parent, false)
        return SeriesViewHolder(v)
    }

    override fun getItemCount(): Int { return items.size }


    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {

        Image(this.context).loadImageSerie(items[position].img, holder.mImg)

        holder.mImg.setOnClickListener(View.OnClickListener {
            val intent = Intent(this.context, SerieActivity::class.java)
            val bundle : Bundle = Bundle()
            bundle.putSerializable("serie",items[position])
            intent.putExtra("bundle",bundle)
            startActivity(this.context,intent,null)
        })
    }

    class SeriesViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val mImg = v.img
    }



}