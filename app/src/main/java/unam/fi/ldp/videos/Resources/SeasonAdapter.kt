package unam.fi.ldp.videos.Resources
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import unam.fi.ldp.videos.R
import java.text.SimpleDateFormat
import android.widget.*
import kotlinx.android.synthetic.main.season_list.view.*
import unam.fi.ldp.videos.Database.MoreVideoModel.Season


class SeasonAdapter(val context: Context, var listItemsTxt: ArrayList<Season>) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.season_list, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // setting adapter item height programatically.
        val params = view.layoutParams
        params.height = 200
        view.layoutParams = params
        Image(this.context).loadImageSeason(listItemsTxt[position].img, vh.img)
        vh.name.text = listItemsTxt.get(position).name
        vh.production.text = SimpleDateFormat("dd/MM/yyyy").format(listItemsTxt[position].dateProduction)
        vh.premier.text = SimpleDateFormat("dd/MM/yyyy").format(listItemsTxt[position].datePremiere)

        return view
    }

    override fun getItem(position: Int): Season? {
        return if (listItemsTxt!=null && position!=-1) listItemsTxt[position] else null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(v: View) {

        val img: ImageView = v.imgSeason
        val name: TextView = v.name
        val production: TextView = v.production
        val premier: TextView = v.premiere

    }
}
