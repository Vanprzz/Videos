package unam.fi.ldp.videos.Resources
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import unam.fi.ldp.videos.R
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.category_list.view.*
import unam.fi.ldp.videos.Resources.CategoryAdapter.CategoryViewHolder
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideoModel.Category

class CategoryAdapter (val context : Context): RecyclerView.Adapter<CategoryViewHolder>() {
    private val items : ArrayList<Category> = ManageMoreVideoDB(this.context).getCategorys()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_list, parent, false)
        return CategoryViewHolder(v)

    }

    override fun getItemCount(): Int { return items.size }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.mSeries.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL, false)
        holder.mSeries.adapter = SeriesAdapter(this.context,items[position].id)
        holder.mText?.text = items[position].name
    }

    class CategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val mText = v.text!!
        val mSeries = v.content_series!!
    }


}