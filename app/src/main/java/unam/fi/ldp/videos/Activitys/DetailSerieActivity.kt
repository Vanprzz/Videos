package unam.fi.ldp.videos.Activitys
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_serie.*
import unam.fi.ldp.videos.Database.MoreVideoModel
import unam.fi.ldp.videos.R
import java.text.SimpleDateFormat

class DetailSerieActivity : AppCompatActivity() {
    private var serie : MoreVideoModel.Serie? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_serie)

        try {
            val bundle: Bundle = intent.extras.getBundle("bundle")
            serie = bundle.getSerializable("serie") as MoreVideoModel.Serie

            unam.fi.ldp.videos.Resources.Image(this).loadImageSerie(serie!!.img,img_d)
            title_d.text = serie!!.title
            language_d.text = serie!!.language
            date_d.text = SimpleDateFormat("dd/MM/yyyy").format(serie!!.year)
            syn_d.text = serie!!.synopsis
        }catch (e : NullPointerException){
            finish()
        }
    }


}
