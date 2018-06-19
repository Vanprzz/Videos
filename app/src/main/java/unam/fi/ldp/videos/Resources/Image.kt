package unam.fi.ldp.videos.Resources
/**
 * PEREZ URIBE CESAR IVAN
 * */
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_signup.*
import unam.fi.ldp.videos.R
import java.io.File



class Image(var context: Context){
    fun loadImageUser(u : String?, img : ImageView){

        val options : RequestOptions =  RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .priority(Priority.HIGH)
                .circleCrop()

        if(u == null) {
            Glide.with(context).load("").
                    apply(options).
                    into(img)
        }else if(u.contains("/storage/")){
            Glide.with(context).load(Uri.fromFile(File(u!!))).
                    apply(options).
                    into(img)
        }else {
            Glide.with(context).load(u!!).
                    apply(options).
                    into(img)
        }


    }

    fun loadImageUser(b : Bitmap, img : ImageView){
        val options :RequestOptions=  RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(android.R.drawable.ic_delete)
                .priority(Priority.HIGH)
                .circleCrop()

        Glide.with(context).load(b).
                apply(options).
                into(img)
    }

    fun loadImageSerie(u : String,img : ImageView){
        val options : RequestOptions =  RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_videocam_black_24dp)
                .error(R.drawable.ic_videocam_off_black_24dp)
                .priority(Priority.HIGH)


        Glide.with(context).load(u).
                apply(options).
                into(img)
    }

    fun loadImageSeason(u : String,img : ImageView){
        val options : RequestOptions =  RequestOptions()
                .centerInside()
                .placeholder(R.drawable.ic_dashboard_black_24dp)
                .error(R.drawable.ic_dashboard_black_24dp)
                .priority(Priority.HIGH)

        Glide.with(context).load(u).
                apply(options).
                into(img)
    }

    fun loadPictureHolder(u:String,img : ImageView){

        val options : RequestOptions =  RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_local_movies_black_24dp)
                .error(android.R.drawable.ic_delete)
                .priority(Priority.HIGH)


        Glide.with(context).load(u).
                apply(options).
                into(img)
    }


}