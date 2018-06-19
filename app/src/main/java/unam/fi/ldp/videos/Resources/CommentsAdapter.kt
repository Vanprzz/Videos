package unam.fi.ldp.videos.Resources

/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.comments_list.view.*
import unam.fi.ldp.videos.Database.ManageMoreVideoDB
import unam.fi.ldp.videos.Database.MoreVideo
import unam.fi.ldp.videos.Database.MoreVideoModel.Comment
import unam.fi.ldp.videos.Database.MoreVideoModel.User
import unam.fi.ldp.videos.R
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class CommentsAdapter(var context: Context, var id_Type : String, var user : User)  :
        RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    private var manageMoreVideoDB = ManageMoreVideoDB(this.context)

    private var items : ArrayList< Pair<User?,Comment> > = getComment()

    private var like:Int? = null
    private var dislike:Int? = null


    private fun getComment(): ArrayList<Pair<User?, Comment>> {
        val commentList: ArrayList<Pair<User?,Comment>> = ArrayList()

        var comments : ArrayList<Comment> = ArrayList()
        comments = if(id_Type.contains("SEA")) manageMoreVideoDB.getSeasonComments(id_Type)
                    else manageMoreVideoDB.getChapterComments(id_Type)

        for (comment in comments){
            commentList.add(Pair(manageMoreVideoDB.getUser(MoreVideo.UsersHeaders.ID+"=?",arrayOf(comment.id_User) )
                    ,comment ))
        }

        return commentList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.comments_list, parent, false)
        return CommentsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {

        if(items[position].first!!.lastname == null) {
            holder.mNameUser.text =   items[position].first!!.name
        }else{
            holder.mNameUser.text = items[position].first!!.name + " " + items[position].first!!.lastname
        }

        holder.mDateComment.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(items[position].second.date)
        holder.mTextComment.text = items[position].second!!.comment
        Image(context).loadImageUser(items[position].first!!.img , holder.mImgUser)

        getPunctuation(holder,items[position].second.id)

        holder.mLike.setOnClickListener {
            val punt : Int? = manageMoreVideoDB.getCommentPunctuationUser(user.id, items[position].second.id)
            if( punt == null){
                manageMoreVideoDB.insertCommentPunctuation(1,user.id, items[position].second.id)
            }else if(punt != 1){
                manageMoreVideoDB.updateCommentPunctuationUser(1,user.id, items[position].second.id)
            }else{
                manageMoreVideoDB.deleteCommentPunctuation(user.id, items[position].second.id)
            }
            getPunctuation(holder,items[position].second.id)
        }
        holder.mDislike.setOnClickListener {
            val punt : Int? = manageMoreVideoDB.getCommentPunctuationUser(user.id, items[position].second.id)
            if( punt ==null){
                manageMoreVideoDB.insertCommentPunctuation(0,user.id,items[position].second.id)
            }else if(punt != 0){
                manageMoreVideoDB.updateCommentPunctuationUser(0,user.id, items[position].second.id)
            }else{
                manageMoreVideoDB.deleteCommentPunctuation(user.id, items[position].second.id)
            }
            getPunctuation(holder,items[position].second.id)
        }
    }

    class CommentsViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val mImgUser = v.user_comment_img!!
        val mNameUser = v.user_comment_name!!
        val mDateComment = v.comment_date!!
        val mTextComment = v.comment_text!!
        val mLike = v.like_comment!!
        val mDislike = v.dislike_comment!!
    }


    private fun getPunctuation(holder:CommentsViewHolder,idComment:String) {
        var punctuation = manageMoreVideoDB.getCommentsPunctuation(idComment!!)
        like = punctuation.first
        dislike = punctuation.second
        holder.mLike.text = like.toString()
        holder.mDislike.text = dislike.toString()
    }


}