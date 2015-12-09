package by.dev.android.dao

class DbSchema {

    object NewsTable {

        val NAME = "news"

        object Cols {
            val _ID = "_id"
            val TITLE = "title"
            val PUB_DATE = "pub_date"
            val DESCRIPTION = "description"
            val FULL_TEXT = "full_text"
            val LINK = "link"
            val GUID = "guid"
        }
    }

    object EnclosureTable {

        val NAME = "enclosure"

        object Cols {
            val _ID = "_id"
            val URL = "url"
            val TYPE = "type"
            val NEWS_ID = "news_id"
        }
    }
}