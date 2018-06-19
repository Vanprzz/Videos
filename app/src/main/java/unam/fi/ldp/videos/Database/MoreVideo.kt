package unam.fi.ldp.videos.Database
/**
 * PEREZ URIBE CESAR IVAN
 * */

import java.util.UUID


class MoreVideo  {

    internal interface Tables {
        companion object {
            const val DB_VERSION = 1
            const val DB_NAME           = "morevideo.db"
            const val TABLE_SERIES      = "series"
            const val TABLE_SEASONS     = "seasons"
            const val TABLE_CHAPTERS    = "chapters"
            const val TABLE_SUBTITLES   = "subtitles"
            const val TABLE_COMMENTS    = "comments"
            const val TABLE_USERS       = "users"
            const val TABLE_CATEGORY    = "category"
            const val TABLE_SEASONCOMMENTS      = "season_comments"
            const val TABLE_CHAPTERSCOMMENTS    = "chapters_comments"
            const val TABLE_SEASONPUNCTUATION   = "season_punctuation"
            const val TABLE_CHAPTERPUNCTUATION  = "chapter_punctuation"
            const val TABLE_COMMENTPUNCTUATION  = "comment_puactuation"
        }
    }

    internal interface UsersHeaders {
        companion object {
            const val ID = "id"
            const val NAME = "name"
            const val LASTNAME = "lastname"
            const val EMAIL = "email"
            const val PASSWORD = "password"
            const val BD = "bd"
            const val IMG = "img"
            const val MONEY = "money"
            const val TYPE = "type"
        }
    }

    internal interface SeriesHeaders {
        companion object {
            const val ID = "id"
            const val IMG = "img"
            const val TITLE = "title"
            const val YEAR = "year"
            const val SYNOPSIS = "synopsis"
            const val LANGUAGE = "language"
            const val ID_CATEGORY = "id_category"
        }
    }

    internal interface SeasonsHeaders {
        companion object {
            const val ID = "id"
            const val IMG = "img"
            const val TITLE = "title"
            const val PRODUCTION = "production"
            const val PREMIER = "premiere"
            const val ID_SERIES = "id_series"
        }
    }

    internal interface ChaptersHeaders {
        companion object {
            const val ID = "id"
            const val IMG = "img"
            const val TITLE = "title"
            const val DURATION = "duration"
            const val SYNOPSIS = "synopsis"
            const val PRICE = "price"
            const val ID_SEASONS = "id_seasons"
        }
    }

    internal interface CommentsHeaders {
        companion object {
            const val ID = "id"
            const val COMMENT = "comment"
            const val DATE = "date"
            const val ID_USERS = "id_users"
            }
    }

    internal interface SeasonCommentsHeaders {
        companion object {
            const val ID = "id"
            const val ID_COMMENT = "id_comment"
            const val ID_SEASON = "id_season"
        }
    }

    internal interface ChapterCommentsHeaders {
        companion object {
            const val ID = "id"
            const val ID_COMMENT = "id_comment"
            const val ID_CHAPTER = "id_chapter"
        }
    }

    internal interface SeasonPunctuationHeaders {
        companion object {
            const val ID = "id"
            const val PUNCTUATION = "punctuation"
            const val ID_USERS = "id_users"
            const val ID_SEASON = "id_season"
        }
    }

    internal interface ChapterPunctuationHeaders {
        companion object {
            const val ID = "id"
            const val PUNCTUATION = "punctuation"
            const val ID_USERS = "id_users"
            const val ID_CHAPTER = "id_chapter"
        }
    }

    internal interface CommentsPunctuationHeaders {
        companion object {
            const val ID = "id"
            const val PUNCTUATION = "punctuation"
            const val ID_USERS = "id_users"
            const val ID_COMMENT = "id_comment"

        }
    }

    internal interface SubtitlesHeaders {
        companion object {
            const val ID = "id"
            const val AUTHOR = "author"
            const val LANGUAGE = "language"
            const val ID_CHAPTERS = "id_chapter"
        }
    }

    internal interface CategoryHeaders {
        companion object {
            const val ID = "id"
            const val NAME = "name"
        }
    }

    class IdSerie : SeriesHeaders {
        companion object {
            fun generateSerieID(): String {
                return "SER-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdSeason : SeasonsHeaders {
        companion object {
            fun generateSeasonID(): String {
                return "SEA-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdChapter : ChaptersHeaders {
        companion object {
            fun generateChapterID(): String {return "CHA-" + UUID.randomUUID().toString()}
        }
    }

    class IdCategory : CategoryHeaders {
        companion object {
            fun generateCategoryID(): String {return "CAT-" + UUID.randomUUID().toString()}
        }
    }

    class IdSubtitle : SubtitlesHeaders {
        companion object {
            fun generateSubtitleID(): String {
                return "SUB-" + UUID.randomUUID().toString()
            }
        }
    }


    class IdUser : UsersHeaders {
        companion object {
            fun generateUserID(): String {
                return "USR-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdCommnet : CommentsHeaders {
        companion object {
            fun generateCommentID(): String {
                return "CMT-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdSeasonComment : SeasonCommentsHeaders {
        companion object {
            fun generateSeasonCommentID(): String {
                return "CMTSN-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdChapterComment : ChapterCommentsHeaders {
        companion object {
            fun generateChapterCommentID(): String {
                return "CMTCR-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdSeasonPunctuation : SeasonPunctuationHeaders {
        companion object {
            fun generateSeasonPunctuationID(): String {
                return "PSN-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdChapterPunctuation : ChapterPunctuationHeaders {
        companion object {
            fun generateChapterPunctuationID(): String {
                return "PCR-" + UUID.randomUUID().toString()
            }
        }
    }

    class IdCommentPunctuation : CommentsPunctuationHeaders {
        companion object {
            fun generateCommentPunctuationID(): String {
                return "PCT-" + UUID.randomUUID().toString()
            }
        }
    }

}