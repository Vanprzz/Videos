package unam.fi.ldp.videos.Database
/**
 * PEREZ URIBE CESAR IVAN
 * */

import java.io.Serializable
import java.util.*

class MoreVideoModel  {

    class Category(var id:String,
                   var name:String
    ) :Serializable{}

    class Serie( var id :String,
                 var img:String,
                 var title: String,
                 var year:Date,
                 var synopsis:String,
                 var language:String,
                 var idCategory:String
    ):Serializable{}

    class Season(var id :String,
                 var img: String,
                 var name :String,
                 var dateProduction :Date,
                 var datePremiere :Date,
                 var id_Serie:String
    ): Serializable {}

    class Chapter(var id:String,
                  var img:String,
                  var name:String,
                  var duration:Int,
                  var price :Int,
                  var synopsis:String,
                  var idSeason:String
    ): Serializable{}

    class Subtitle(var id:String,
                   var author : String,
                   var language : String,
                   var id_Chapter:String
    ): Serializable {}

    class Comment(var id:String,
                  var comment: String,
                  var date: Date,
                  var id_User:String
    ): Serializable {}

    class User (var id: String,
                var name: String,
                var lastname :String? ,
                var email: String,
                var password :String,
                var bd: Date,
                var img: String?,
                var money:Int,
                var type:Int
    ): Serializable {}
}

