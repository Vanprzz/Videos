package unam.fi.ldp.videos.Database
/**
 * PEREZ URIBE CESAR IVAN
 * */

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
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
import unam.fi.ldp.videos.Database.MoreVideoModel.*
import unam.fi.ldp.videos.Database.MoreVideo.*
import unam.fi.ldp.videos.Database.MoreVideo.Tables.Companion.TABLE_SUBTITLES
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageMoreVideoDB(context: Context)
{
    private var dataBase: MoreVideoDB? = MoreVideoDB(context)

    fun getDb(): SQLiteDatabase { return dataBase!!.writableDatabase }

    // |||||||||||OPC USER |||||||||||

    private val tableUser = TABLE_USERS
    private val proyUser = arrayOf(
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

    fun getUsers(selection:String,selectionArgs:Array<String>) : ArrayList<User> {
        val db = dataBase!!.readableDatabase
        var users: ArrayList<User> = ArrayList()
        try{
            db.beginTransaction()

            val c: Cursor = db.query(tableUser,proyUser,selection,selectionArgs,null,null,UsersHeaders.NAME +" ASC")

            while (c.moveToNext()) {

                users.add(
                        User(c.getString(c.getColumnIndex(UsersHeaders.ID)),
                            c.getString(c.getColumnIndex(UsersHeaders.NAME)),
                            c.getString(c.getColumnIndex(UsersHeaders.LASTNAME)),
                            c.getString(c.getColumnIndex(UsersHeaders.EMAIL)),
                            c.getString(c.getColumnIndex(UsersHeaders.PASSWORD)),
                            Date( c.getColumnIndex(UsersHeaders.BD).toLong() ),
                            c.getString(c.getColumnIndex(UsersHeaders.IMG)),
                            c.getInt(c.getColumnIndex(UsersHeaders.MONEY)),
                            c.getInt(c.getColumnIndex(UsersHeaders.TYPE))
                )
                )
            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return users
    }

    fun getUser(selection:String,selectionArgs:Array<String>) : User? {
        val db = dataBase!!.readableDatabase
        var user:User? = null
        try{
            db.beginTransaction()

            val c: Cursor = db.query(tableUser,proyUser,selection,selectionArgs,null,null,null)

            while (c.moveToNext()) {

                    user = User(c.getString(c.getColumnIndex(UsersHeaders.ID)),
                            c.getString(c.getColumnIndex(UsersHeaders.NAME)),
                            c.getString(c.getColumnIndex(UsersHeaders.LASTNAME)),
                            c.getString(c.getColumnIndex(UsersHeaders.EMAIL)),
                            c.getString(c.getColumnIndex(UsersHeaders.PASSWORD)),
                            Date( c.getColumnIndex(UsersHeaders.BD).toLong() ),
                            c.getString(c.getColumnIndex(UsersHeaders.IMG)),
                            c.getInt(c.getColumnIndex(UsersHeaders.MONEY)),
                            c.getInt(c.getColumnIndex(UsersHeaders.TYPE))
                    )
            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return user
    }

    fun insertUser(user: User) :Boolean{

        val db = dataBase!!.writableDatabase
        val idUser = MoreVideo.IdUser.generateUserID() // Generar Pk
        val values = ContentValues()

        values.put(UsersHeaders.ID, idUser)
        values.put(UsersHeaders.NAME, user.name)
        values.put(UsersHeaders.LASTNAME, user.lastname)
        values.put(UsersHeaders.EMAIL, user.email)
        values.put(UsersHeaders.PASSWORD, user.password)
        values.put(UsersHeaders.BD, user.bd.time)
        values.put(UsersHeaders.IMG, user.img)
        values.put(UsersHeaders.MONEY, 100)
        values.put(UsersHeaders.TYPE, user.type)

        val result = db.insertOrThrow(tableUser, null, values)
        return result > 0
    }

    fun updateUser(newUser: User): Boolean {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(UsersHeaders.NAME, newUser.name)
        values.put(UsersHeaders.LASTNAME, newUser.lastname)
        values.put(UsersHeaders.EMAIL, newUser.email)
        values.put(UsersHeaders.PASSWORD, newUser.password)
        values.put(UsersHeaders.IMG, newUser.img)
        values.put(UsersHeaders.MONEY, newUser.money)
        values.put(UsersHeaders.TYPE, newUser.type)

        val whereClause = String.format("%s=?", UsersHeaders.ID)
        val whereArgs = arrayOf(newUser.id)
        val result = db.update(tableUser, values, whereClause, whereArgs)


        return result > 0
    }

    fun deleteUser(idUser: String) {

        val db = dataBase!!.writableDatabase
        val whereClause = UsersHeaders.ID + "=?"
        val whereArgs = arrayOf(idUser)
        //val result = db.delete(TABLE_USERS, whereClause, whereArgs)
        db.beginTransaction()

        /*val joinComment:String  = "INNER JOIN $TABLE_COMMENTS ON "+CommentsHeaders.ID_USERS +" = "+UsersHeaders.ID
        val joinSeasonPunt:String  = "INNER JOIN $TABLE_SEASONPUNCTUATION ON "+SeasonPunctuationHeaders.ID_USERS +" = "+UsersHeaders.ID
        val joinChapterPun:String  = "INNER JOIN $TABLE_CHAPTERPUNCTUATION ON "+ChapterPunctuationHeaders.ID_USERS +" = "+UsersHeaders.ID
        val joinCommentPun:String  = "INNER JOIN $TABLE_COMMENTPUNCTUATION ON "+CommentsPunctuationHeaders.ID_USERS +" = "+UsersHeaders.ID


        val selectComment :String = "SELECT * FROM $TABLE_COMMENTS $joinComment WHERE "+CommentsHeaders.ID_USERS + " = "+idUser
        val selectSeasonP :String = "SELECT * FROM $TABLE_SEASONPUNCTUATION $joinSeasonPunt WHERE "+SeasonPunctuationHeaders.ID_USERS + " = "+idUser
        val selectChapterP :String = "SELECT * FROM $TABLE_CHAPTERPUNCTUATION $joinChapterPun WHERE "+ChapterPunctuationHeaders.ID_USERS + " = "+idUser
        val selectCommentP :String = "SELECT * FROM $TABLE_COMMENTPUNCTUATION $joinCommentPun WHERE "+CommentsPunctuationHeaders.ID_USERS + " = "+idUser
        */
        val where:String = UsersHeaders.ID +"='"+ idUser+"'"
        db.execSQL("PRAGMA foreign_keys = ON")
        db.execSQL(" DELETE FROM $TABLE_USERS  WHERE $where ")
        /*
        db.execSQL(" DELETE FROM $TABLE_COMMENTS  WHERE $where ")
        db.execSQL(" DELETE FROM $TABLE_SEASONPUNCTUATION  WHERE $where ")
        db.execSQL(" DELETE FROM $TABLE_CHAPTERPUNCTUATION  WHERE $where ")
        db.execSQL(" DELETE FROM $TABLE_COMMENTPUNCTUATION  WHERE $where ")*/
        db.endTransaction()
    }


    // ||||||||||| END OPC USER |||||||||||

    // |||||||||||OPC CATEGORY |||||||||||

    private val tableCategory = TABLE_CATEGORY

    private val proyCategory = arrayOf(
            CategoryHeaders.ID,
            CategoryHeaders.NAME)

    fun getCategorys(): ArrayList<Category> {
        val db = dataBase!!.readableDatabase
        val list : ArrayList<Category> = ArrayList<Category>()
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableCategory, proyCategory, null, null, null, null, null)

            while (c.moveToNext()) {

                list.add(Category (
                        c.getString(c.getColumnIndex(CategoryHeaders.ID)),
                        c.getString(c.getColumnIndex(CategoryHeaders.NAME))
                ))
            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return list
    }

    // ||||||||||| END OPC CATEGORY |||||||||||

    // |||||||||||OPC SERIE |||||||||||
    private val tableSerie = TABLE_SERIES +
            " INNER JOIN "+ TABLE_CATEGORY+" ON "+
            SeriesHeaders.ID_CATEGORY + "=" + TABLE_CATEGORY + "." + CategoryHeaders.ID

    private val proySerie = arrayOf(
                                    TABLE_SERIES + "." + SeriesHeaders.ID,
                                    SeriesHeaders.IMG,
                                    SeriesHeaders.TITLE,
                                    SeriesHeaders.YEAR,
                                    SeriesHeaders.SYNOPSIS,
                                    SeriesHeaders.LANGUAGE,
                                    SeriesHeaders.ID_CATEGORY
                                    )

    fun getSeries(idCategory:String): ArrayList<Serie> {
        val db = dataBase!!.readableDatabase
        val selection = SeriesHeaders.ID_CATEGORY+"=?"
        val list : ArrayList<Serie> = ArrayList<Serie>()
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableSerie, proySerie, selection, arrayOf(idCategory), null, null, null)

            while (c.moveToNext()) {
                list.add(
                        Serie(
                                c.getString(c.getColumnIndex(SeriesHeaders.ID)),
                                c.getString(c.getColumnIndex(SeriesHeaders.IMG)),
                                c.getString(c.getColumnIndex(SeriesHeaders.TITLE)),
                                SimpleDateFormat("dd/MM/yyyy").parse(c.getString(c.getColumnIndex(SeriesHeaders.YEAR))),
                                c.getString(c.getColumnIndex(SeriesHeaders.SYNOPSIS)),
                                c.getString(c.getColumnIndex(SeriesHeaders.LANGUAGE)),
                                c.getString(c.getColumnIndex(SeriesHeaders.ID_CATEGORY)))
                )

            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return list
    }

    // |||||||||||END OPC SERIE |||||||||||

    // |||||||||||OPC SEASON |||||||||||

    private val tableSeason = TABLE_SEASONS + " INNER JOIN "+
            TABLE_SERIES+" ON "+
            SeasonsHeaders.ID_SERIES+"="+Tables.TABLE_SERIES+"."+SeriesHeaders.ID

    private val proySeason = arrayOf(
            TABLE_SEASONS + "." + SeasonsHeaders.ID,
            TABLE_SEASONS + "." + SeasonsHeaders.TITLE,
            TABLE_SEASONS + "." + SeasonsHeaders.IMG,
            SeasonsHeaders.PRODUCTION,
            SeasonsHeaders.PREMIER,
            SeasonsHeaders.ID_SERIES)



    fun getSeasons(idSerie:String): ArrayList<Season> {
        val db = dataBase!!.readableDatabase
        val selection = SeasonsHeaders.ID_SERIES+"=?"
        val list : ArrayList<Season> = ArrayList<Season>()
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableSeason, proySeason, selection, arrayOf(idSerie), null, null, null)
            val date: SimpleDateFormat =  SimpleDateFormat("dd/MM/yyyy")

            while (c.moveToNext()) {
                list.add(
                        Season(c.getString(c.getColumnIndex(SeasonsHeaders.ID)),
                                c.getString(c.getColumnIndex(SeasonsHeaders.IMG)),
                                c.getString(c.getColumnIndex(SeasonsHeaders.TITLE)),
                                date.parse(c.getString(c.getColumnIndex(SeasonsHeaders.PRODUCTION))),
                                date.parse(c.getString(c.getColumnIndex(SeasonsHeaders.PREMIER))),
                                c.getString(c.getColumnIndex(MoreVideo.SeasonsHeaders.ID_SERIES))
                        )
                )

            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return list

    }

    // |||||||||||END OPC SEASON |||||||||||

    // |||||||||||OPC CHAPTER |||||||||||

    private val tableChapter = TABLE_CHAPTERS +
            " INNER JOIN "+TABLE_SEASONS+" ON "+
            ChaptersHeaders.ID_SEASONS+"="+TABLE_SEASONS+"."+SeasonsHeaders.ID


    private val proyChapter = arrayOf<String>(
            TABLE_CHAPTERS + "." + ChaptersHeaders.ID,
            TABLE_CHAPTERS + "." + ChaptersHeaders.IMG,
            TABLE_CHAPTERS + "." + ChaptersHeaders.TITLE,
            ChaptersHeaders.DURATION,
            ChaptersHeaders.PRICE,
            ChaptersHeaders.SYNOPSIS,
            ChaptersHeaders.ID_SEASONS)


    fun getChapter(idSeason: String) : ArrayList<Chapter> {
        val db = dataBase!!.readableDatabase
        var selection = ChaptersHeaders.ID_SEASONS+"=?"
        val chapters : ArrayList<Chapter> = ArrayList()
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableChapter, proyChapter, selection, arrayOf(idSeason), null, null, null)

            while (c.moveToNext()) {

                chapters.add(
                        Chapter(
                                c.getString(c.getColumnIndex(ChaptersHeaders.ID)),
                                c.getString(c.getColumnIndex(ChaptersHeaders.IMG)),
                                c.getString(c.getColumnIndex(ChaptersHeaders.TITLE)),
                                c.getInt(c.getColumnIndex(ChaptersHeaders.DURATION)),
                                c.getInt(c.getColumnIndex(ChaptersHeaders.PRICE)),
                                c.getString(c.getColumnIndex(ChaptersHeaders.SYNOPSIS)),
                                c.getString(c.getColumnIndex(ChaptersHeaders.ID_SEASONS))
                        )
                )
            }
            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return chapters
    }

    // |||||||||||END OPC CHAPTER |||||||||||

    // ||||||||||| COMMENTS |||||||||||
    private val tableComment =
            TABLE_COMMENTS +
                    " INNER JOIN "+TABLE_USERS +" ON "+
                    CommentsHeaders.ID_USERS+"="+TABLE_USERS+"."+UsersHeaders.ID

    private val proyComment = arrayOf(
            TABLE_COMMENTS + "." + UsersHeaders.ID,
            CommentsHeaders.COMMENT,
            CommentsHeaders.DATE,
            CommentsHeaders.ID_USERS
            )

    fun insertComment(comment:Comment) {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(CommentsHeaders.ID, comment.id)
        values.put(CommentsHeaders.DATE, comment.date.time)
        values.put(CommentsHeaders.COMMENT, comment.comment )
        values.put(CommentsHeaders.ID_USERS, comment.id_User)

        db.insertOrThrow(MoreVideo.Tables.TABLE_COMMENTS, null, values)
    }

    fun deleteComments(idUser: String): Boolean {

        val db = dataBase!!.writableDatabase
        val whereClause = CommentsHeaders.ID_USERS + "=?"
        val whereArgs = arrayOf(idUser)

        val result = db.delete(tableComment, whereClause, whereArgs)

        return result > 0
    }


    // ||||||||||| END |||||||||||

    // ||||||||||| SUBTITLE |||||||||||
    private val tableSubtitle =
            TABLE_SUBTITLES +
                    " INNER JOIN "+ TABLE_CHAPTERS +" ON "+
                    SubtitlesHeaders.ID_CHAPTERS+"="+ TABLE_CHAPTERS+"."+ChaptersHeaders.ID

    private val proySubtitle = arrayOf(
            TABLE_SUBTITLES + "." + SubtitlesHeaders.ID,
            SubtitlesHeaders.AUTHOR,
            SubtitlesHeaders.LANGUAGE,
            SubtitlesHeaders.ID_CHAPTERS
    )

    fun getSubtitle(idChapter: String) : ArrayList<Subtitle> {
        val db = dataBase!!.readableDatabase
        var selection = SubtitlesHeaders.ID_CHAPTERS+"=?"
        val subtitles:ArrayList<Subtitle> = ArrayList()
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableSubtitle, proySubtitle, selection, arrayOf(idChapter), null, null, SubtitlesHeaders.LANGUAGE+" ASC")

            while (c.moveToNext()) {
                subtitles.add(
                        Subtitle(c.getString(c.getColumnIndex(SubtitlesHeaders.ID)),
                                c.getString(c.getColumnIndex(SubtitlesHeaders.AUTHOR)),
                                c.getString(c.getColumnIndex(SubtitlesHeaders.LANGUAGE)),
                                c.getString(c.getColumnIndex(SubtitlesHeaders.ID_CHAPTERS)))
                )
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return subtitles
    }

    fun insertSubtitle(subtitle:Subtitle) {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(SubtitlesHeaders.ID, subtitle.id)
        values.put(SubtitlesHeaders.AUTHOR, subtitle.author)
        values.put(SubtitlesHeaders.LANGUAGE, subtitle.language )
        values.put(SubtitlesHeaders.ID_CHAPTERS, subtitle.id_Chapter)

        db.insertOrThrow(TABLE_SUBTITLES, null, values)
    }

    // ||||||||||| END |||||||||||

    // ||||||||||| SEASONCOMMENT|||||||||||
    private val tableSeasonComment =
            TABLE_SEASONCOMMENTS +
                    " INNER JOIN "+ TABLE_COMMENTS +" ON "+
                    SeasonCommentsHeaders.ID_COMMENT+"="+ TABLE_COMMENTS+"."+CommentsHeaders.ID+
                    " INNER JOIN "+ TABLE_SEASONS +" ON "+
                    SeasonCommentsHeaders.ID_SEASON+"="+ TABLE_SEASONS+"."+SeasonsHeaders.ID

    private val proySeasonComment = arrayOf(
            TABLE_SEASONCOMMENTS + "." + SeasonCommentsHeaders.ID,
            SeasonCommentsHeaders.ID_COMMENT,
            SeasonCommentsHeaders.ID_SEASON
    )

    fun getSeasonComments(idSeason: String) : ArrayList<Comment> {
        val db = dataBase!!.readableDatabase
        var selection = SeasonCommentsHeaders.ID_SEASON+"=?"
        val comments:ArrayList<Comment> = ArrayList()
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableSeasonComment, proyComment, selection, arrayOf(idSeason), null, null, TABLE_COMMENTS+"."+BaseColumns._ID+" DESC")

            while (c.moveToNext()) {
                comments.add( Comment(
                        c.getString(c.getColumnIndex(SeasonCommentsHeaders.ID)),
                        c.getString(c.getColumnIndex(CommentsHeaders.COMMENT)),
                        Date(c.getLong(c.getColumnIndex(CommentsHeaders.DATE))),
                        c.getString(c.getColumnIndex(CommentsHeaders.ID_USERS))
                        )
                )
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return comments
    }


    fun insertSeasonComment(idComment:String,idSeason: String) {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(SeasonCommentsHeaders.ID, IdSeasonComment.generateSeasonCommentID())
        values.put(SeasonCommentsHeaders.ID_COMMENT, idComment)
        values.put(SeasonCommentsHeaders.ID_SEASON, idSeason)

        db.insertOrThrow(TABLE_SEASONCOMMENTS, null, values)
    }


    // ||||||||||| END SEASONCOMMENT |||||||||||

    // ||||||||||| CHAPTERSCOMMENT|||||||||||
    private val tableChapterComment =
            TABLE_CHAPTERSCOMMENTS +
                    " INNER JOIN "+ TABLE_COMMENTS +" ON "+
                    ChapterCommentsHeaders.ID_COMMENT+"="+ TABLE_COMMENTS+"."+CommentsHeaders.ID+
                    " INNER JOIN "+ TABLE_CHAPTERS +" ON "+
                    ChapterCommentsHeaders.ID_CHAPTER+"="+ TABLE_CHAPTERS+"."+ChaptersHeaders.ID

    private val proyChapterComment = arrayOf(
            TABLE_CHAPTERSCOMMENTS + "." + SeasonCommentsHeaders.ID,
            ChapterCommentsHeaders.ID_COMMENT,
            ChapterCommentsHeaders.ID_CHAPTER
    )

    fun getChapterComments(idChapter: String) : ArrayList<Comment> {
        val db = dataBase!!.readableDatabase
            var selection = ChapterCommentsHeaders.ID_CHAPTER+"=?"
        val comments:ArrayList<Comment> = ArrayList()
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableChapterComment, proyComment, selection, arrayOf(idChapter), null, null, TABLE_COMMENTS+"."+BaseColumns._ID+" DESC")

            while (c.moveToNext()) {
                comments.add( Comment(
                        c.getString(c.getColumnIndex(ChapterCommentsHeaders.ID)),
                        c.getString(c.getColumnIndex(CommentsHeaders.COMMENT)),
                        Date(c.getLong(c.getColumnIndex(CommentsHeaders.DATE))),
                        c.getString(c.getColumnIndex(CommentsHeaders.ID_USERS))
                )
                )
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return comments
    }


    fun insertChapterComment(idComment:String,idChapter: String) {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(ChapterCommentsHeaders.ID, IdChapterComment.generateChapterCommentID())
        values.put(ChapterCommentsHeaders.ID_COMMENT, idComment)
        values.put(ChapterCommentsHeaders.ID_CHAPTER, idChapter)

        db.insertOrThrow(TABLE_CHAPTERSCOMMENTS, null, values)
    }


    // ||||||||||| END CHAPTERCOMMENT |||||||||||


    // ||||||||||| SEASONPUNCTUATION|||||||||||
    private val tableSeasonPunctuation =
            TABLE_SEASONPUNCTUATION +
                    " INNER JOIN "+ TABLE_USERS +" ON "+
                    SeasonPunctuationHeaders.ID_USERS+"="+ TABLE_USERS +"."+UsersHeaders.ID+
                    " INNER JOIN "+ TABLE_SEASONS +" ON "+
                    SeasonPunctuationHeaders.ID_SEASON+"="+ TABLE_SEASONS +"."+SeasonsHeaders.ID

    private val proySeasonPunctuation = arrayOf(
            TABLE_SEASONPUNCTUATION + "." + SeasonCommentsHeaders.ID,
            SeasonPunctuationHeaders.PUNCTUATION,
            SeasonPunctuationHeaders.ID_USERS,
            SeasonPunctuationHeaders.ID_SEASON
    )

    fun getSeasonPunctuation(idSeason: String) : Pair<Int,Int> {
        val db = dataBase!!.readableDatabase
        var selection = SeasonPunctuationHeaders.ID_SEASON+"=?"

        var LIKE: Int = 0
        var DISLIKE: Int = 0
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableSeasonPunctuation, proySeasonPunctuation, selection, arrayOf(idSeason), null, null, null)

            while (c.moveToNext()) {
                if( c.getInt(c.getColumnIndex(SeasonPunctuationHeaders.PUNCTUATION)) == 1 ){ LIKE++}
                else {DISLIKE++}
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return Pair<Int,Int>(LIKE,DISLIKE)
    }


    fun getSeasonPunctuationUser(idUser: String,idSeason: String) : Int? {
        val db = dataBase!!.readableDatabase
        var selection = SeasonPunctuationHeaders.ID_USERS+"=? AND "+SeasonPunctuationHeaders.ID_SEASON+"=?"

        var punctuation: Int? = null
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableSeasonPunctuation, proySeasonPunctuation, selection, arrayOf(idUser,idSeason), null, null, null)

            while (c.moveToNext()) {
                punctuation = c.getInt(c.getColumnIndex(SeasonPunctuationHeaders.PUNCTUATION))
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return punctuation
    }

    fun updateSeasonPunctuationUser(punctuation: Int,idUser: String,idSeason: String) : Boolean {
        val db = dataBase!!.readableDatabase

        val values = ContentValues()

        values.put(SeasonPunctuationHeaders.PUNCTUATION,punctuation)

        val whereClause = String.format("%s=? AND %s=?", SeasonPunctuationHeaders.ID_USERS,SeasonPunctuationHeaders.ID_SEASON)
        val whereArgs = arrayOf(idUser,idSeason)
        val result = db.update(TABLE_SEASONPUNCTUATION, values, whereClause, whereArgs)


        return result > 0
    }


    fun insertSeasonPunctuation(punctuation: Int,idUser:String,idSeason: String) {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(SeasonPunctuationHeaders.ID, IdSeasonPunctuation.generateSeasonPunctuationID())
        values.put(SeasonPunctuationHeaders.PUNCTUATION, punctuation)
        values.put(SeasonPunctuationHeaders.ID_USERS, idUser)
        values.put(SeasonPunctuationHeaders.ID_SEASON, idSeason)

        db.insertOrThrow(TABLE_SEASONPUNCTUATION, null, values)
    }

    fun deleteSeasonPunctuation(idUser: String,idSeason: String): Boolean {

        val db = dataBase!!.writableDatabase
        val whereClause = SeasonPunctuationHeaders.ID_USERS + "=? AND "+SeasonPunctuationHeaders.ID_SEASON+ "=?"
        val whereArgs = arrayOf(idUser,idSeason)
        val result = db.delete(TABLE_SEASONPUNCTUATION, whereClause, whereArgs)

        return result > 0
    }

    // ||||||||||| END SEASONPUNCTUATION |||||||||||


    // ||||||||||| CHAPTERSPUNCTUATION|||||||||||
    private val tableChapterPunctuation =
            TABLE_CHAPTERPUNCTUATION +
                    " INNER JOIN "+ TABLE_USERS +" ON "+
                    ChapterPunctuationHeaders.ID_USERS+"="+ TABLE_USERS +"."+UsersHeaders.ID+
                    " INNER JOIN "+ TABLE_CHAPTERS +" ON "+
                    ChapterPunctuationHeaders.ID_CHAPTER+"="+ TABLE_CHAPTERS +"."+ChaptersHeaders.ID

    private val proyChapterPunctuation = arrayOf(
            TABLE_CHAPTERPUNCTUATION + "." + ChapterPunctuationHeaders.ID,
            ChapterPunctuationHeaders.PUNCTUATION,
            ChapterPunctuationHeaders.ID_USERS,
            ChapterPunctuationHeaders.ID_CHAPTER
    )

    fun getChapterPunctuation(idChapter: String) : Pair<Int,Int> {
        val db = dataBase!!.readableDatabase
        var selection = ChapterPunctuationHeaders.ID_CHAPTER+"=?"

        var LIKE: Int = 0
        var DISLIKE: Int = 0
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableChapterPunctuation, proyChapterPunctuation, selection, arrayOf(idChapter), null, null, null)

            while (c.moveToNext()) {
                if( c.getInt(c.getColumnIndex(ChapterPunctuationHeaders.PUNCTUATION)) == 1 ){ LIKE++}
                else {DISLIKE++}
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return Pair<Int,Int>(LIKE,DISLIKE)
    }


    fun getChapterPunctuationUser(idUser: String,idChapter: String) : Int? {
        val db = dataBase!!.readableDatabase
        var selection = ChapterPunctuationHeaders.ID_USERS+"=? AND "+ChapterPunctuationHeaders.ID_CHAPTER+"=?"

        var punctuation: Int? = null
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableChapterPunctuation, proyChapterPunctuation, selection, arrayOf(idUser,idChapter), null, null, null)

            while (c.moveToNext()) {
                punctuation = c.getInt(c.getColumnIndex(ChapterPunctuationHeaders.PUNCTUATION))
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return punctuation
    }

    fun updateChapterPunctuationUser(punctuation: Int,idUser: String,idChapter: String) : Boolean {
        val db = dataBase!!.readableDatabase

        val values = ContentValues()

        values.put(SeasonPunctuationHeaders.PUNCTUATION,punctuation)

        val whereClause = String.format("%s=? AND %s=?", ChapterPunctuationHeaders.ID_USERS,ChapterPunctuationHeaders.ID_CHAPTER)
        val whereArgs = arrayOf(idUser,idChapter)
        val result = db.update(TABLE_CHAPTERPUNCTUATION, values, whereClause, whereArgs)


        return result > 0
    }




    fun insertChapterPunctuation(punctuation: Int,idUser:String,idChapter: String) {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(ChapterPunctuationHeaders.ID, IdChapterPunctuation.generateChapterPunctuationID())
        values.put(ChapterPunctuationHeaders.PUNCTUATION, punctuation)
        values.put(ChapterPunctuationHeaders.ID_USERS, idUser)
        values.put(ChapterPunctuationHeaders.ID_CHAPTER, idChapter)

        db.insertOrThrow(TABLE_CHAPTERPUNCTUATION, null, values)
    }

    fun deleteChapterPunctuation(idUser: String,idChapter: String): Boolean {

        val db = dataBase!!.writableDatabase
        val whereClause = ChapterPunctuationHeaders.ID_USERS + "=? AND "+ChapterPunctuationHeaders.ID_CHAPTER+ "=?"
        val whereArgs = arrayOf(idUser,idChapter)
        val result = db.delete(TABLE_CHAPTERPUNCTUATION, whereClause, whereArgs)

        return result > 0
    }

    // ||||||||||| END CHAPTERSPUNCTUATION |||||||||||


    // ||||||||||| COMMENTPUNCTUATION|||||||||||
    private val tableCommentsPunctuation =
            TABLE_COMMENTPUNCTUATION +
                    " INNER JOIN "+ TABLE_COMMENTS +" ON "+
                    CommentsPunctuationHeaders.ID_COMMENT+"="+ TABLE_COMMENTS +"."+CommentsHeaders.ID

    private val proyCommentsPunctuation = arrayOf(
            TABLE_COMMENTPUNCTUATION + "." + CommentsPunctuationHeaders.ID,
            CommentsPunctuationHeaders.PUNCTUATION,
            TABLE_COMMENTPUNCTUATION+"."+CommentsPunctuationHeaders.ID_USERS,
            CommentsPunctuationHeaders.ID_COMMENT
    )

    fun getCommentsPunctuation(idComment: String) : Pair<Int,Int> {
        val db = dataBase!!.readableDatabase
        var selection = CommentsPunctuationHeaders.ID_COMMENT+"=?"

        var LIKE: Int = 0
        var DISLIKE: Int = 0
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableCommentsPunctuation, proyCommentsPunctuation, selection, arrayOf(idComment), null, null, null)

            while (c.moveToNext()) {
                if( c.getInt(c.getColumnIndex(CommentsPunctuationHeaders.PUNCTUATION)) == 1 ){ LIKE++}
                else {DISLIKE++}
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return Pair<Int,Int>(LIKE,DISLIKE)
    }


    fun getCommentPunctuationUser(idUser: String,idComment: String) : Int? {
        val db = dataBase!!.readableDatabase
        var selection = TABLE_COMMENTPUNCTUATION+"."+CommentsPunctuationHeaders.ID_USERS+"=? AND "+CommentsPunctuationHeaders.ID_COMMENT+"=?"

        var punctuation: Int? = null
        try{
            db.beginTransaction()
            val c: Cursor = db.query(tableCommentsPunctuation, proyCommentsPunctuation, selection, arrayOf(idUser,idComment), null, null, null)

            while (c.moveToNext()) {
                punctuation = c.getInt(c.getColumnIndex(CommentsPunctuationHeaders.PUNCTUATION))
            }

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }
        return punctuation
    }

    fun updateCommentPunctuationUser(punctuation: Int,idUser: String,idComment: String) : Boolean {
        val db = dataBase!!.readableDatabase

        val values = ContentValues()

        values.put(CommentsPunctuationHeaders.PUNCTUATION,punctuation)

        val whereClause = String.format("%s=? AND %s=?", CommentsPunctuationHeaders.ID_USERS,CommentsPunctuationHeaders.ID_COMMENT)
        val whereArgs = arrayOf(idUser,idComment)
        val result = db.update(TABLE_COMMENTPUNCTUATION, values, whereClause, whereArgs)


        return result > 0
    }




    fun insertCommentPunctuation(punctuation: Int,idUser:String,idComment: String) {
        val db = dataBase!!.writableDatabase
        val values = ContentValues()

        values.put(CommentsPunctuationHeaders.ID, IdCommentPunctuation.generateCommentPunctuationID())
        values.put(CommentsPunctuationHeaders.PUNCTUATION, punctuation)
        values.put(CommentsPunctuationHeaders.ID_USERS, idUser)
        values.put(CommentsPunctuationHeaders.ID_COMMENT, idComment)

        db.insertOrThrow(TABLE_COMMENTPUNCTUATION, null, values)
    }


    fun deleteCommentPunctuation(idUser: String,idComment: String): Boolean {

        val db = dataBase!!.writableDatabase
        val whereClause = CommentsPunctuationHeaders.ID_USERS + "=? AND "+CommentsPunctuationHeaders.ID_COMMENT+ "=?"
        val whereArgs = arrayOf(idUser,idComment)
        val result = db.delete(TABLE_COMMENTPUNCTUATION, whereClause, whereArgs)

        return result > 0
    }

    // ||||||||||| END COMMENTPUNCTUATION |||||||||||



}


