package unam.fi.ldp.videos.Database
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.DB_NAME
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.DB_VERSION
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_USERS
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_CATEGORY
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_SERIES
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_SEASONS
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_SEASONCOMMENTS
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_SEASONPUNCTUATION
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_CHAPTERS
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_CHAPTERSCOMMENTS
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_CHAPTERPUNCTUATION
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_COMMENTPUNCTUATION
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_COMMENTS
import unam.fi.ldp.videos.Database.MoreVideo.*
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_SUBTITLES
import kotlin.collections.ArrayList

class MoreVideoDB (context: Context) :
        SQLiteOpenHelper(context,  DB_NAME, null ,  DB_VERSION) {


    private val context: Context = context
    val category :Array<String> = arrayOf( "ACTION","AVENTURE","ANIMATION","COMEDY","ROMANCE" )


    internal interface References {
        companion object {

            val ID_SERIES = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                    TABLE_SERIES ,SeriesHeaders.ID)

            val ID_SEASONS = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                    TABLE_SEASONS, SeasonsHeaders.ID)

            val ID_CHAPTERS = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                    TABLE_CHAPTERS, ChaptersHeaders.ID)

            val ID_SUBTITLES = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                    TABLE_SUBTITLES, SubtitlesHeaders.ID)

            val ID_CATEGORY = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                    TABLE_CATEGORY, CategoryHeaders.ID)

            val ID_COMMENTS = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                    TABLE_COMMENTS, CommentsHeaders.ID)

            val ID_USERS = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                    TABLE_USERS, UsersHeaders.ID)



        }
    }



    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        if (!db!!.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true)
            } else {
                db.execSQL("PRAGMA foreign_keys=ON")
            }
        }
    }


    override fun onCreate(db: SQLiteDatabase) {

        //USERS
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "%s TEXT UNIQUE NOT NULL," +
                                    "%s TEXT NOT NULL," +
                                    "%s TEXT ," +
                                    "%s TEXT NOT NULL," +
                                    "%s TEXT NOT NULL," +
                                    "%s DATATIME,"+
                                    "%s TEXT,"+
                                    "%s INT ,"+
                                    "%s INT NOT NULL)",

                        TABLE_USERS, BaseColumns._ID ,
                                    UsersHeaders.ID,
                                    UsersHeaders.NAME,
                                    UsersHeaders.LASTNAME,
                                    UsersHeaders.EMAIL,
                                    UsersHeaders.PASSWORD,
                                    UsersHeaders.BD,
                                    UsersHeaders.IMG,
                                    UsersHeaders.MONEY,
                                    UsersHeaders.TYPE
                                    )
        )
        //CATEGORY
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s TEXT)" ,

                        TABLE_CATEGORY, BaseColumns._ID ,
                                CategoryHeaders.ID,
                                CategoryHeaders.NAME
                )
        )

        //SERIES
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "%s TEXT UNIQUE NOT NULL," +
                                    "%s TEXT NOT NULL," +
                                    "%s TEXT NOT NULL," +
                                    "%s DATATIME ," +
                                    "%s TEXT ," +
                                    "%s INTEGER,"+
                                    "%s TEXT NOT NULL ," +
                                        "FOREIGN KEY (%s) %s)",

                        TABLE_SERIES, BaseColumns._ID ,
                                    SeriesHeaders.ID,
                                    SeriesHeaders.IMG,
                                    SeriesHeaders.TITLE,
                                    SeriesHeaders.YEAR,
                                    SeriesHeaders.SYNOPSIS,
                                    SeriesHeaders.LANGUAGE,
                                    SeriesHeaders.ID_CATEGORY,
                                    SeriesHeaders.ID_CATEGORY,References.ID_CATEGORY
                )
        )

        //SEASONS
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s TEXT NOT NULL," +
                                "%s TEXT NOT NULL," +
                                "%s DATATIME ," +
                                "%s DATATIME ," +
                                "%s TEXT NOT NULL,"+
                                "FOREIGN KEY (%s) %s)",

                        TABLE_SEASONS, BaseColumns._ID ,
                                SeasonsHeaders.ID,
                                SeasonsHeaders.IMG,
                                SeasonsHeaders.TITLE,
                                SeasonsHeaders.PRODUCTION,
                                SeasonsHeaders.PREMIER,
                                SeasonsHeaders.ID_SERIES,
                                SeasonsHeaders.ID_SERIES,References.ID_SERIES
                )
        )


        //CHAPTER
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s TEXT  NULL," +
                                "%s TEXT NOT NULL," +
                                "%s INTEGER NOT NULL," +
                                "%s INTEGER NOT NULL," +
                                "%s TEXT," +
                                "%s TEXT NOT NULL,"+
                                "FOREIGN KEY (%s) %s)",

                        TABLE_CHAPTERS, BaseColumns._ID ,
                                ChaptersHeaders.ID,
                                ChaptersHeaders.IMG,
                                ChaptersHeaders.TITLE,
                                ChaptersHeaders.PRICE,
                                ChaptersHeaders.DURATION,
                                ChaptersHeaders.SYNOPSIS,
                                ChaptersHeaders.ID_SEASONS,
                                ChaptersHeaders.ID_SEASONS,References.ID_SEASONS
                )
        )

        //COMMENTS
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s TEXT NOT NULL," +
                                "%s DATETIME NOT NULL," +
                                "%s TEXT NOT NULL,"+
                                "FOREIGN KEY (%s) %s)",

                        TABLE_COMMENTS, BaseColumns._ID ,
                                CommentsHeaders.ID,
                                CommentsHeaders.COMMENT,
                                CommentsHeaders.DATE,
                                CommentsHeaders.ID_USERS,
                                CommentsHeaders.ID_USERS,References.ID_USERS
                )
        )

        //SUBTITLES
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s TEXT NOT NULL," +
                                "%s TEXT NOT NULL," +
                                "%s TEXT NOT NULL,"+
                               "FOREIGN KEY (%s) %s)",

                        TABLE_SUBTITLES, BaseColumns._ID ,
                                SubtitlesHeaders.ID,
                                SubtitlesHeaders.AUTHOR,
                                SubtitlesHeaders.LANGUAGE,
                                SubtitlesHeaders.ID_CHAPTERS,
                                SubtitlesHeaders.ID_CHAPTERS,References.ID_USERS
                )
        )

        //COMMENTS season
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s TEXT NOT NULL %s," +
                                "%s TEXT NOT NULL %s)",

                        TABLE_SEASONCOMMENTS, BaseColumns._ID ,
                                SeasonCommentsHeaders.ID,
                                SeasonCommentsHeaders.ID_COMMENT,References.ID_COMMENTS,
                                SeasonCommentsHeaders.ID_SEASON,References.ID_SEASONS
                )
        )

        //COMMENTS CHAPTER
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s TEXT NOT NULL  %s ," +
                                "%s TEXT NOT NULL  %s )",

                        TABLE_CHAPTERSCOMMENTS, BaseColumns._ID ,
                                ChapterCommentsHeaders.ID,
                                ChapterCommentsHeaders.ID_COMMENT,References.ID_COMMENTS,
                                ChapterCommentsHeaders.ID_CHAPTER,References.ID_CHAPTERS
                )
        )



        //PUNCTUATION SEASON
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s BIT  NOT NULL," +
                                "%s TEXT NOT NULL %s ," +
                                "%s TEXT NOT NULL %s)",
                        TABLE_SEASONPUNCTUATION, BaseColumns._ID ,
                                SeasonPunctuationHeaders.ID,
                                SeasonPunctuationHeaders.PUNCTUATION,
                                SeasonPunctuationHeaders.ID_USERS,References.ID_USERS,
                                SeasonPunctuationHeaders.ID_SEASON,References.ID_SEASONS
                )
        )


        //PUNCTUATION CHAPTERS
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s BIT  NOT NULL," +
                                "%s TEXT NOT NULL %s ," +
                                "%s TEXT NOT NULL %s )",
                        TABLE_CHAPTERPUNCTUATION, BaseColumns._ID ,
                                ChapterPunctuationHeaders.ID,
                                ChapterPunctuationHeaders.PUNCTUATION,
                                ChapterPunctuationHeaders.ID_USERS,References.ID_USERS,
                                ChapterPunctuationHeaders.ID_CHAPTER,References.ID_CHAPTERS
                )
        )

        //PUNCTUATION COMMENTS
        db.execSQL(
                String.format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT UNIQUE NOT NULL," +
                                "%s BIT  NOT NULL," +
                                "%s TEXT NOT NULL %s ," +
                                "%s TEXT NOT NULL %s )",
                        TABLE_COMMENTPUNCTUATION, BaseColumns._ID ,
                                CommentsPunctuationHeaders.ID,
                                CommentsPunctuationHeaders.PUNCTUATION,
                                CommentsPunctuationHeaders.ID_USERS,References.ID_USERS,
                                CommentsPunctuationHeaders.ID_COMMENT,References.ID_COMMENTS
                )
        )


        genereteCategorys(db)
        genereteSeries(db)
        genereteSeason(db)
        genereteChapters(db)
        genereteSubtitle(db)

        genereteRoot(db)
    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_SERIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SEASONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHAPTERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUBTITLES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")

        onCreate(db)
    }

    private fun genereteCategorys(db: SQLiteDatabase) {
        //CATEGORY
        for(  item in category) {
            val id = IdCategory.generateCategoryID()
            db.execSQL(
                    String.format(
                            "INSERT INTO %s (%s,%s) VALUES ('%s','%s')",
                            TABLE_CATEGORY, CategoryHeaders.ID, CategoryHeaders.NAME,
                            id, item
                    ))
        }
    }

    private fun genereteRoot(db: SQLiteDatabase) {
        //ROOT
        db.execSQL(
                String.format(
                        "INSERT INTO %s (%s ,%s ,%s ,%s ,%s ,%s, %s) VALUES ('%s','root',null,'ldp@admin.com','Admin123.',null,0)",
                        TABLE_USERS,
                        UsersHeaders.ID,
                        UsersHeaders.NAME,
                        UsersHeaders.LASTNAME,
                        UsersHeaders.EMAIL,
                        UsersHeaders.PASSWORD,
                        UsersHeaders.IMG,
                        UsersHeaders.TYPE,

                        IdUser.generateUserID()
                )
        )
    }


    private fun searchCategory(db: SQLiteDatabase) : ArrayList<MoreVideoModel.Category>{
        val c: Cursor = db.query(MoreVideo.Tables.TABLE_CATEGORY,null,null,null,null,null,null)

        val categoryList : ArrayList<MoreVideoModel.Category> = ArrayList()
        while (c.moveToNext()) {

            categoryList.add(MoreVideoModel.Category(
                    c.getString(c.getColumnIndex(CategoryHeaders.ID)),
                    c.getString(c.getColumnIndex(CategoryHeaders.NAME))
                    )
            )
        }

        return categoryList
    }

    private fun searchSerie(db: SQLiteDatabase,title :String): String {
        var idSerie :String = ""
        val c= db.rawQuery("select id from $TABLE_SERIES where title='$title' ",null)
        while (c.moveToNext()) {
             idSerie = c.getString(c.getColumnIndex(SeriesHeaders.ID))
        }
        return idSerie
    }

    private fun searchSeason(db: SQLiteDatabase,idSerie :String): ArrayList<String> {
        val idSeason :ArrayList<String> = ArrayList()

        val b= db.rawQuery("select seasons.id from $TABLE_SEASONS inner join series on id_series=series.id where id_series='$idSerie'",null)
        while (b.moveToNext()) {
                idSeason.add( b.getString(b.getColumnIndex(MoreVideo.SeasonsHeaders.ID)))
        }
        return idSeason
    }

    private fun searchChapter(db: SQLiteDatabase,idSeason :String): ArrayList<String> {
        val idChapter :ArrayList<String> = ArrayList()

        val b= db.rawQuery("select chapters.id from $TABLE_CHAPTERS inner join seasons on id_seasons=seasons.id where id_seasons='$idSeason'",null)
        while (b.moveToNext()) {
            idChapter.add( b.getString(b.getColumnIndex(MoreVideo.SeasonsHeaders.ID)))
        }
        return idChapter
    }

    private fun addSerie(db: SQLiteDatabase,values :ContentValues){
        db.execSQL(
                String.format(
                        "INSERT INTO %s (%s ,%s ,%s ,%s ,%s ,%s ,%s ) VALUES ('%s','%s','%s','%s','%s','%s','%s')",
                        TABLE_SERIES,
                        SeriesHeaders.ID,
                        SeriesHeaders.IMG,
                        SeriesHeaders.TITLE,
                        SeriesHeaders.YEAR,
                        SeriesHeaders.SYNOPSIS,
                        SeriesHeaders.LANGUAGE,
                        SeriesHeaders.ID_CATEGORY,

                        MoreVideo.IdSerie.generateSerieID(),
                        values.getAsString(SeriesHeaders.IMG),
                        values.getAsString(SeriesHeaders.TITLE),
                        values.getAsString(SeriesHeaders.YEAR),
                        values.getAsString(SeriesHeaders.SYNOPSIS),
                        values.getAsString(SeriesHeaders.LANGUAGE),
                        values.getAsString(SeriesHeaders.ID_CATEGORY)
                )
        )
    }


    private fun addSeasons(db: SQLiteDatabase,values :ContentValues) {
                db.execSQL(
                        String.format(
                                "INSERT INTO %s (%s ,%s ,%s ,%s ,%s,%s) VALUES ('%s' ,'%s' ,'%s' ,'%s' ,'%s','%s')",
                                TABLE_SEASONS,
                                SeasonsHeaders.ID,
                                SeasonsHeaders.IMG,
                                SeasonsHeaders.TITLE,
                                SeasonsHeaders.PRODUCTION,
                                SeasonsHeaders.PREMIER,
                                SeasonsHeaders.ID_SERIES,

                                MoreVideo.IdSeason.generateSeasonID(),
                                values.getAsString(SeasonsHeaders.IMG),
                                values.getAsString(SeasonsHeaders.TITLE),
                                values.getAsString(SeasonsHeaders.PRODUCTION),
                                values.getAsString(SeasonsHeaders.PREMIER),
                                values.getAsString(SeasonsHeaders.ID_SERIES)
                        )
        )

    }

    private fun addChapters(db: SQLiteDatabase,values :ContentValues ) {
        db.execSQL(
                String.format(
                        "INSERT INTO %s (%s ,%s ,%s ,%s ,%s,%s,%s) VALUES ('%s' ,'%s' ,'%s' ,'%s' ,'%s' ,'%s','%s')",
                        TABLE_CHAPTERS,
                        ChaptersHeaders.ID,
                        ChaptersHeaders.IMG,
                        ChaptersHeaders.TITLE,
                        ChaptersHeaders.PRICE,
                        ChaptersHeaders.DURATION,
                        ChaptersHeaders.SYNOPSIS,
                        ChaptersHeaders.ID_SEASONS,

                        MoreVideo.IdChapter.generateChapterID(),
                        values.getAsString(ChaptersHeaders.IMG),
                        values.getAsString(ChaptersHeaders.TITLE),
                        values.getAsString(ChaptersHeaders.PRICE),
                        values.getAsString(ChaptersHeaders.DURATION),
                        values.getAsString(ChaptersHeaders.SYNOPSIS),
                        values.getAsString(ChaptersHeaders.ID_SEASONS)
                        )
                )

    }


    fun addSubtitle(db:SQLiteDatabase,values : ContentValues){
        //SUBTITLES
        db.execSQL(
                String.format(
                        "INSERT INTO %s (%s ,%s ,%s ,%s ) VALUES ('%s' ,'%s' ,'%s' ,'%s')",
                        TABLE_SUBTITLES,
                        SubtitlesHeaders.ID,
                        SubtitlesHeaders.AUTHOR,
                        SubtitlesHeaders.LANGUAGE,
                        SubtitlesHeaders.ID_CHAPTERS,

                        values.getAsString(SubtitlesHeaders.ID),
                        values.getAsString(SubtitlesHeaders.AUTHOR),
                        values.getAsString(SubtitlesHeaders.LANGUAGE),
                        values.getAsString(SubtitlesHeaders.ID_CHAPTERS)
                )
        )
    }

    private fun genereteSeries(db: SQLiteDatabase) {
        val values :ContentValues = ContentValues()
        val category = searchCategory(db)

        //ACCION
        values.put(MoreVideo.SeriesHeaders.IMG,"https://ia.media-imdb.com/images/M/MV5BMTkxMDk5NTQ3MF5BMl5BanBnXkFtZTgwNzg3ODU3NDM@._V1_SY1000_CR0,0,674,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Jessica Jones")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2015")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Following the tragic end of her brief superhero career, Jessica Jones tries to rebuild her life as a private investigator, dealing with cases involving people with remarkable abilities in New York City.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[0].id)

        addSerie(db,values)
        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMTc5MjAzNjYwM15BMl5BanBnXkFtZTgwMTI5NzE1MzI@._V1_SY1000_CR0,0,674,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"The Punisher")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2017")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"After the murder of his family, Marine veteran Frank Castle became a vigilante known as The Punisher with only one goal in mind, to avenge them.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[0].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMjQyOTQ3Njc5M15BMl5BanBnXkFtZTgwMzgyNjAxNTM@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Legion")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2017")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"David Haller is a troubled young man diagnosed as schizophrenic, but after a strange encounter, he discovers special powers that will change his life forever.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[0].id)

        addSerie(db,values)
        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BNWU4NmY3MTMtMTBmMi00NjFjLTkwMmItYWZhZWUwNDg5M2ExXkEyXkFqcGdeQXVyNDUyOTg3Njg@._V1_SY1000_CR0,0,674,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"The Defenders")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2017")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Set a few months after the events of the second season of Daredevil, and a month after the events of Iron Fist, the vigilantes Daredevil, Jessica Jones, Luke Cage, and Iron Fist team up in New York City to fight a common enemy: The Hand.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[0].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BZTQzNmEwZTMtZGNkNC00YjQ5LThhYzMtZTBhNzUzODI5ZjRjXkEyXkFqcGdeQXVyMjM5NzU3OTM@._V1_SY1000_SX800_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"The Flash")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2014")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"After being struck by lightning, Barry Allen wakes up from his coma to discover he s been given the power of super speed, becoming the Flash, fighting crime in Central City.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[0].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMTUwOTgyNTQ1M15BMl5BanBnXkFtZTgwNDEyNzM3MzI@._V1_SY1000_SX800_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Arrow")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2012")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Spoiled billionaire playboy Oliver Queen is missing and presumed dead when his yacht is lost at sea. He returns five years later a changed man, determined to clean up the city as a hooded vigilante armed with a bow.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[0].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BNjk0ZjEzNGEtOTg5Yy00ZTU3LWE1NTQtNWI4MTJmMTlkMTVhXkEyXkFqcGdeQXVyNDc0NDgwODI@._V1_SY1000_CR0,0,666,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"The 100")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2014")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Set ninety-seven years after a nuclear war has destroyed civilization, when a spaceship housing humanity s lone survivors sends one hundred juvenile delinquents back to Earth, in hopes of possibly re-populating the planet.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[0].id)

        addSerie(db,values)

//ADVENTURE
        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BNDYyNzk1NzYwOF5BMl5BanBnXkFtZTgwMTQ0Nzc4MzI@._V1_SY1000_CR0,0,738,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Vikings")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2013")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"The world of the Vikings is brought to life through the journey of Ragnar Lothbrok, the first Viking to emerge from Norse legend and onto the pages of history - a man on the edge of myth.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[1].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMWY3NTljMjEtYzRiMi00NWM2LTkzNjItZTVmZjE0MTdjMjJhL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyNTQ4NTc5OTU@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Sherlock")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2010")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[1].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMjQ4OTg3ODkyMl5BMl5BanBnXkFtZTgwMjI0OTg5NDM@._V1_SY1000_CR0,0,674,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"A series of unfortunate events")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2017")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"After the loss of their parents in a mysterious fire, the three Baudelaire children face trials and tribulations attempting to uncover dark family secrets.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[1].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMjE3NTQ1NDg1Ml5BMl5BanBnXkFtZTgwNzY2NDA0MjI@._V1_SY1000_CR0,0,674,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Game of Thrones")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2014")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Nine noble families fight for control over the mythical lands of Westeros, while an ancient enemy returns after being dormant for thousands of years.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[1].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BNzhlY2E5NDUtYjJjYy00ODg3LWFkZWQtYTVmMzU4ZWZmOWJkXkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SY1000_CR0,0,647,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Lost")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2004")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"The survivors of a plane crash are forced to work together in order to survive on a seemingly deserted tropical island.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[1].id)

        addSerie(db,values)



///ANIMACION

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BYmNlMTYzNDMtZjI5NS00NTdjLTlkYWMtMWMzZWJjODY2YzAzXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SY1000_CR0,0,701,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Psycho-Pass")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2014")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Believing in humanity and order, policewoman Akane Tsunemori obeys the ruling, computerized, precognitive Sibyl System. But when she faces a criminal mastermind who can elude this \"perfect\" system, she questions both Sibyl and herself.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Japanese")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[2].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BN2NlM2Y5Y2MtYjU5Mi00ZjZiLWFjNjMtZDNiYzJlMjhkOWZiXkEyXkFqcGdeQXVyNjc2NjA5MTU@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Re: Zero kara hajimeru isekai seikatsu")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2016")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Shortly after being summoned to a new world, Subaru Natsuki and his new female companion are brutally murdered. But then he awakes to find himself in the same alley, with the same thugs, the same girl, and the day begins to repeat.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Japanese")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[2].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BYWFkM2M0M2ItZWY3Yy00NmIzLWFhOGYtODU0N2FhZmNlNmRlXkEyXkFqcGdeQXVyMzExMzk5MTQ@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Naruto")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2007")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Naruto Uzumaki, is a loud, hyperactive, adolescent ninja who constantly searches for approval and recognition, as well as to become Hokage, who is acknowledged as the leader and strongest of all ninja in the village.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Japanese")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[2].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BY2I2MzI1ODYtMWRlOS00MzdhLWEyOWEtYWJhNmFiZTIxMGJhXkEyXkFqcGdeQXVyMTExNDQ2MTI@._V1_SY1000_CR0,0,666,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Dragon Ball Super")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2015")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"The continuing adventures of the mighty warrior Son Goku, as he encounters new worlds and new warriors to fight.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Japanese")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[2].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMDM0MDA3NzYtMDE1MS00YjZmLWJmNjQtNzgxYzlhMmMyZjQ2XkEyXkFqcGdeQXVyNjk1Njg5NTA@._V1_SY1000_CR0,0,701,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Yu-Gi-Oh!")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2000")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Yugi Moto solves an Ancient Egyptian Puzzle and brings forth a dark and powerful alter ego. Whenever he and his friends are threatened by evil in Duel Monster Card Game, this alter ego breaks out to save them.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Japanese")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[2].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BN2RkZjk0ODctNWE0MS00MjFlLTliMDMtZTFhMWM2NTAxMDg1XkEyXkFqcGdeQXVyNjc0ODMyMDc@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Angel Beats!")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2010")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Rebellious teens fight in armed combat against one dispassionate girl s supernatural powers in an afterlife high school.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Japanese")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[2].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BZTU4YTAzNjUtZjk2Zi00NGRlLTg2YWUtN2RkNGZmMmY0ODYwXkEyXkFqcGdeQXVyMjc0MjUzMzU@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Mawaru-Penguindrum")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2011")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"A terminally ill girl is revived by a magical penguin spirit. In return, her brothers are sent on a quest for the mysterious  Penguindrum .")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Japanese")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[2].id)

        addSerie(db,values)





        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BNTc2MzM2N2YtZDdiOS00M2I2LWFjOGItMDM3OTA3YjUwNjAxXkEyXkFqcGdeQXVyNzA5NjUyNjM@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Malcolm")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2000")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"A gifted young teen tries to survive life with his dimwitted, dysfunctional family.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[3].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BYzgwMWI4ZGMtOGYyMi00ZmYyLWE0MzUtYmU5MjE1NDQ5MDhmXkEyXkFqcGdeQXVyNjEwNTM2Mzc@._V1_SY1000_CR0,0,675,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Love")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2016")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"A program that follows a couple who must navigate the exhilarations and humiliations of intimacy, commitment and other things they were hoping to avoid.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[3].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMjA0MDc1NTk0Ml5BMl5BanBnXkFtZTgwMTk2ODA5NDM@._V1_SY1000_SX800_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"New Girl")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2011")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"After a bad break-up, Jess, an offbeat young woman, moves into an apartment loft with three single men. Although they find her behavior very unusual, the men support her - most of the time.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[3].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMTk1MjYzOTU2Nl5BMl5BanBnXkFtZTgwMzAxMTg5MTE@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Suits")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2011")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"On the run from a drug deal gone bad, Mike Ross, a brilliant college dropout, finds himself a job working with Harvey Specter, one of New York City s best lawyers.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[3].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BY2FmZTY5YTktOWRlYy00NmIyLWE0ZmQtZDg2YjlmMzczZDZiXkEyXkFqcGdeQXVyNjg4NzAyOTA@._V1_SY1000_CR0,0,666,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Big Bang")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2007")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[3].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMTI5MjUyYjUtMzhmOC00NTc3LWIzMTctMTVlOTA2NjI2NDU2XkEyXkFqcGdeQXVyMzAzNTY3MDM@._V1_SY1000_SX675_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Modern Family")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2009")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Three different, but related families face trials and tribulations in their own uniquely comedic ways.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[3].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMTQ2Njk2MzY2M15BMl5BanBnXkFtZTgwMDkxODg3MDE@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Community")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2009")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"A suspended lawyer is forced to enroll in a community college with an eclectic staff and student body.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[3].id)

        addSerie(db,values)


/////////////romace

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMzU3ODQxMjgzOV5BMl5BanBnXkFtZTgwNDg4NDgzMDI@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Crazy Ex-girlfriend")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2015")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"A young woman abandons a choice job at a law firm and her life in New York in an attempt to find happiness in the unlikely locale of West Covina, California.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[4].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BYjI0ZTBlYTAtNzFjYy00MTIyLWI5ZTAtMmMyMjIxZGE3NTM2XkEyXkFqcGdeQXVyNTQwNTMwNTQ@._V1_SY1000_CR0,0,706,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"I Need Romance")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2011")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"As a hotel concierge manager tries to sort out her relationship with her ex, events take an unforeseen turn when the hotel s heir enters the picture.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[4].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMmIyZjFlMTQtNGMwZi00YWI4LThkYjQtYWE3N2Q1MDllZWYzXkEyXkFqcGdeQXVyNjMxNTM3MzI@._V1_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Bad Romance")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2016")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Yihwa, a single university girl, believes that she doesn t need boys in her life because she can survive without them, and thinks that boys are like iPhones that are only for decoration ...")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[4].id)

        addSerie(db,values)

        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMTEwMDUyMzgyNDVeQTJeQWpwZ15BbWU4MDQ3ODU3ODIy._V1_SY1000_CR0,0,666,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Outlander")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2014")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"An English combat nurse from 1945 is mysteriously swept back in time to 1743.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[4].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMTQ5OTIyMzkzOF5BMl5BanBnXkFtZTgwMDMzOTUxMDI@._V1_SY1000_CR0,0,666,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Younger")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2015")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"After being mistaken for younger than she really is, a single mother decides to take the chance to reboot her career and her love life as a 26-year old.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[4].id)

        addSerie(db,values)


        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BMDVjZDQ2N2MtMzMxYy00ZjliLTg5YjAtNjk1OTUwYjVjNWQ0XkEyXkFqcGdeQXVyNzA5NjUyNjM@._V1_SY1000_CR0,0,667,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"True Blood")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2008")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"Telepathic waitress Sookie Stackhouse encounters a strange new supernatural world when she meets the mysterious Bill, a southern Louisiana gentleman and vampire.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"English")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[4].id)

        addSerie(db,values)



        values.put(MoreVideo.SeriesHeaders.IMG,"https://m.media-amazon.com/images/M/MV5BNDRmNjFmOWYtMjZmMy00ZjBlLWI0N2MtYTcyYTgzOGIxYzY0XkEyXkFqcGdeQXVyMTkzODUwNzk@._V1_SY1000_CR0,0,701,1000_AL_.jpg")
        values.put(MoreVideo.SeriesHeaders.TITLE,"Awkward")
        values.put(MoreVideo.SeriesHeaders.YEAR,"01/01/2011")
        values.put(MoreVideo.SeriesHeaders.SYNOPSIS,"An unpopular 15-year-old gains immediate, yet unwanted, popularity at her high school when the student body mistakes an accident she has for a suicide attempt.")
        values.put(MoreVideo.SeriesHeaders.LANGUAGE,"Eglihs")
        values.put(MoreVideo.SeriesHeaders.ID_CATEGORY,category[4].id)

        addSerie(db,values)


//////////////


    }

    private fun genereteSeason(db :SQLiteDatabase){
        //Jessica Jones
        val idSerie = searchSerie(db,"Jessica Jones" )
        val values : ContentValues = ContentValues()

        values.put(MoreVideo.SeasonsHeaders.IMG,"https://i1.wp.com/chardasuuraj.com/wp-content/uploads/2018/03/729a76706088d491cbfa585d125c041c.jpg?w=300&ssl=1")
        values.put(MoreVideo.SeasonsHeaders.TITLE,"Season 1")
        values.put(MoreVideo.SeasonsHeaders.PRODUCTION,"01/02/2015")
        values.put(MoreVideo.SeasonsHeaders.PREMIER,"17/08/2015")
        values.put(MoreVideo.SeasonsHeaders.ID_SERIES,idSerie)
        addSeasons(db,values)
        //Season 2
        values.put(MoreVideo.SeasonsHeaders.IMG,"https://upload.wikimedia.org/wikipedia/en/f/f0/Jessica_Jones_season_2_poster.jpg")
        values.put(MoreVideo.SeasonsHeaders.TITLE,"Season 2")
        values.put(MoreVideo.SeasonsHeaders.PRODUCTION,"12/04/2017")
        values.put(MoreVideo.SeasonsHeaders.PREMIER,"08/03/2018")
        values.put(MoreVideo.SeasonsHeaders.ID_SERIES,idSerie)
        addSeasons(db,values)


    }

    private fun genereteChapters(db :SQLiteDatabase){
        //Jessica Jones
        val idsSeason = searchSeason(db, searchSerie(db,"Jessica Jones" ) )
        val values : ContentValues = ContentValues()

        values.put(MoreVideo.ChaptersHeaders.IMG,"http://cimg.tvgcdn.net/i/r/2015/10/01/0a757d4f-41bd-4861-a7a0-02e5326053be/resize/900x600/415fcd689c76301b95aec573079b94de/150101-news-jessicajones.jpg")
        values.put(MoreVideo.ChaptersHeaders.TITLE,"AKA Ladies Night")
        values.put(MoreVideo.ChaptersHeaders.PRICE,"9")
        values.put(MoreVideo.ChaptersHeaders.DURATION,"24")
        values.put(MoreVideo.ChaptersHeaders.SYNOPSIS,"Jessica Jones, an alcoholic private investigator \"gifted\" with superhuman strength and flight, delivers a subpoena to strip club owner Gregory Spheeris for lawyer Jeri Hogarth (who is having an affair with her assistant Pam behind the back of her wife Wendy Ross-Hogarth), exposing her abilities to him in the process. While not working, Jones spies on Luke Cage, a bar owner who sees her looking into his bar and offers her free alcohol as a \"Ladies Night\" promotion, leading to the two sleeping together. She leaves upset after seeing a photo of a woman in his bathroom. Jones is approached by Barbara and Bob Shlottman after their daughter Hope began acting differently and disappeared. Jones discovers that Hope is with Kilgrave, a man with mind control abilities who once controlled Jones, leaving her with PTSD, and whom she believed was dead. Jones wants to flee, but is convinced by her friend and foster-sister Trish Walker to help Hope. Jones finds her, but Kilgraves hold is still over Hope, and she murders her parents.")
        values.put(MoreVideo.ChaptersHeaders.ID_SEASONS,idsSeason[0])
        addChapters(db,values)

        values.put(MoreVideo.ChaptersHeaders.IMG,"https://nerdist.com/wp-content/uploads/2015/10/Jessica-Jones1.jpg")
        values.put(MoreVideo.ChaptersHeaders.TITLE,"AKA Crush Syndrome")
        values.put(MoreVideo.ChaptersHeaders.PRICE,"10")
        values.put(MoreVideo.ChaptersHeaders.DURATION,"30")
        values.put(MoreVideo.ChaptersHeaders.SYNOPSIS,"Jones is investigated by Detective Oscar Clemons, who discovers photos she took of Cage. Jones lies to Cage that she had been hired by the husband of a woman who Cage had slept with. Cage, having not known that the woman was married, confronts the woman about it, and when she goes to her husband, he attacks Cage with a group. Jones helps fight off the men, and learns that Cage is gifted with unbreakable skin. Hogarth agrees to represent Shlottman if Jones can prove that Kilgrave exists. Jones remembers leaving Kilgrave to die after he was hit by a bus, and now tracks down the ambulance driver who had picked him up. The driver had donated both his kidneys to Kilgrave, and is now on dialysis. Jones finds the operating doctor who anonymously donated the dialysis machine. He agrees to testify for Shlottman, and reveals that he operated on Kilgrave without anesthesia since that would have blocked Kilgraves abilities. When Hogarth meets with Shlottman, the latter reveals that Jones was once under Kilgraves control as well.")
        values.put(MoreVideo.ChaptersHeaders.ID_SEASONS,idsSeason[0])
        addChapters(db,values)

        values.put(MoreVideo.ChaptersHeaders.IMG,"https://media3.s-nbcnews.com/j/newscms/2018_09/2342576/180226-jessica-jones-season-2-ac-614p_7b4dda9778eab974b597a49727aaff07.focal-760x380.jpg")
        values.put(MoreVideo.ChaptersHeaders.TITLE,"AKA Its Called Whiskey")
        values.put(MoreVideo.ChaptersHeaders.PRICE,"11")
        values.put(MoreVideo.ChaptersHeaders.DURATION,"31")
        values.put(MoreVideo.ChaptersHeaders.SYNOPSIS,"Jones and Cage bond over their mutual powers, while Jones also tracks down a surgical grade anesthetic to subdue Kilgrave with. Hogarth, now divorcing her wife and unwilling to risk herself and her reputation despite the doctors testimony, organizes for Walker to interview Shlottman about Kilgrave live on her radio talk show. Walker asks that anyone who believes they have been approached or controlled by Kilgrave contact Hogarth, and then publicly insults Kilgrave on air. Angered, Kilgrave sends police sergeant Will Simpson to kill Walker, with Jones having to use the anesthetic on Walker to convince Simpson that he has carried out his orders. Jones follows Simpson back to Kilgrave, who orders Simpson to walk off a balcony. Jones again convinces Simpson that he has carried out this order by knocking him out and taking him to the street below, telling him as he wakes that she caught him. Searching through the house that Kilgrave was occupying, Jones discovers a room full of photographs of her.")
        values.put(MoreVideo.ChaptersHeaders.ID_SEASONS,idsSeason[0])
        addChapters(db,values)

        //Season 2
        values.put(MoreVideo.ChaptersHeaders.IMG,"https://nerdist.com/wp-content/uploads/2015/10/Jessica-Jones1.jpg")
        values.put(MoreVideo.ChaptersHeaders.TITLE,"AKA Start at the Beginning")
        values.put(MoreVideo.ChaptersHeaders.PRICE,"9")
        values.put(MoreVideo.ChaptersHeaders.DURATION,"25")
        values.put(MoreVideo.ChaptersHeaders.SYNOPSIS,"Super-powered private investigator Jessica Jones has become known as a vigilante hero around New York City since she killed her tormentor Kilgrave. Trish Walker, Joness best friend and adopted sister, attempts to convince Jones to investigate her past and IGH, the company that gave her abilities, but Jones is not interested. Walker is dealing with declining ratings for her radio show Trish Talk, and sees her ex-boyfriend Will Simpson—who was also experimented on by IGH—following her. Pryce Cheng, another investigator, seeks to absorb Jones into his company at the request of lawyer Jeri Hogarth. When Jones attacks and injures Cheng, he plans to sue her with Hogarth, who is already facing a lawsuit from her former assistant and lover. Jones is approached by Robert Coleman, who also calls himself \"Whizzer\" and was given superspeed by IGH. When he is killed in an apparent construction accident, Jones traces his medication to an abandoned building which she remembers being taken to and experimented on.")
        values.put(MoreVideo.ChaptersHeaders.ID_SEASONS,idsSeason[1])
        addChapters(db,values)

        values.put(MoreVideo.ChaptersHeaders.IMG,"https://media3.s-nbcnews.com/j/newscms/2018_09/2342576/180226-jessica-jones-season-2-ac-614p_7b4dda9778eab974b597a49727aaff07.focal-760x380.jpg")
        values.put(MoreVideo.ChaptersHeaders.TITLE,"AKA Freak Accident")
        values.put(MoreVideo.ChaptersHeaders.PRICE,"11")
        values.put(MoreVideo.ChaptersHeaders.DURATION,"31")
        values.put(MoreVideo.ChaptersHeaders.SYNOPSIS,"Jones goes to the home of Miklos Kozlov, the IGH doctor who had experimented on Simpson. She finds a shiva for Kozlov, who has died in a \"freak accident\". One of Kozlovs army patients believes that Simpson is behind this, and Jones suspects that he also killed Coleman. Walker asks Malcolm Ducasse, Joness neighbor and work partner, to help her rather than further involve the reluctant Jones. Walker confronts Maximilian Tatum, a director, about the sexual relationship they had when she was a child actress working for him, threatening to publicly reveal this unless he helps her get access to records at a hospital he has influence over. After Tatum refuses, Walker runs into Simpson. Jones also arrives, having tracked Walker down when she did not answer her phone. Simpson claims that someone else who was experimented on by IGH has killed Kozlov and Coleman, and that he is just there to protect Walker since she was noticed investigating IGH. This other person soon appears and kills Simpson while Jones gets Walker to safety.")
        values.put(MoreVideo.ChaptersHeaders.ID_SEASONS,idsSeason[1])
        addChapters(db,values)
    }

    private fun genereteSubtitle(db :SQLiteDatabase){
        //Jessica Jones
        val idsSeason = searchSeason(db, searchSerie(db,"Jessica Jones" ) )
        var idsChapter = searchChapter(db, idsSeason[0] )
        val values : ContentValues = ContentValues()

        //CHAPTER FIRST JESSICA JONES
        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Cesar Perez")
        values.put(SubtitlesHeaders.LANGUAGE,"Spanish")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[0])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Ivan Uribe")
        values.put(SubtitlesHeaders.LANGUAGE,"English")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[0])
        addSubtitle(db,values)



        //CHAPTER SECOND JESSICA JONES
        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Cesar Perez")
        values.put(SubtitlesHeaders.LANGUAGE,"Spanish")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[1])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Ivan Uribe")
        values.put(SubtitlesHeaders.LANGUAGE,"English")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[1])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Anonimo")
        values.put(SubtitlesHeaders.LANGUAGE,"French")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[1])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Anonimo")
        values.put(SubtitlesHeaders.LANGUAGE,"German")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[1])
        addSubtitle(db,values)

        //Season 2
        idsChapter = searchChapter(db, idsSeason[1] )

        //CHAPTER FIRST JESSICA JONES
        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Cesar Perez")
        values.put(SubtitlesHeaders.LANGUAGE,"Spanish")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[0])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Ivan Uribe")
        values.put(SubtitlesHeaders.LANGUAGE,"English")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[0])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Anonimo")
        values.put(SubtitlesHeaders.LANGUAGE,"French")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[0])
        addSubtitle(db,values)


        //CHAPTER SECOND JESSICA JONES
        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Cesar Perez")
        values.put(SubtitlesHeaders.LANGUAGE,"Spanish")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[1])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Ivan Uribe")
        values.put(SubtitlesHeaders.LANGUAGE,"English")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[1])
        addSubtitle(db,values)

        values.put(SubtitlesHeaders.ID,IdSubtitle.generateSubtitleID())
        values.put(SubtitlesHeaders.AUTHOR,"Anonimo")
        values.put(SubtitlesHeaders.LANGUAGE,"French")
        values.put(SubtitlesHeaders.ID_CHAPTERS,idsChapter[1])
        addSubtitle(db,values)




    }


}