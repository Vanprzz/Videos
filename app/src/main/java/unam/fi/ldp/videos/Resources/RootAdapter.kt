package unam.fi.ldp.videos.Resources
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.category_list.view.*
import kotlinx.android.synthetic.main.root_list.view.*
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.MoreVideoModel
import unam.fi.ldp.videos.R

class RootAdapter (val context : Context): RecyclerView.Adapter<RootAdapter.RootViewHolder>() {
    private var manageMoreVideoDB = ManageMoreVideoDB(context)
    private val items : ArrayList<MoreVideoModel.User> = manageMoreVideoDB.getUsers(MoreVideo.UsersHeaders.TYPE +"=? ", arrayOf("1"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.root_list, parent, false)
        return RootViewHolder(v)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RootViewHolder, position: Int) {

        Image(context).loadImageUser(items[position].img,holder.mImg)
        holder.mName.text = items[position].name
        holder.mEmail.text = items[position].email
        holder.mDelete.setOnClickListener {
            manageMoreVideoDB.deleteUser(items[position].id)
            notifyItemRemoved(position)
        }
    }


    class RootViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val mImg = v.img_usr!!
        val mName = v.name_usr!!
        val mEmail = v.email_usr!!
        val mDelete = v.delete!!
    }


}