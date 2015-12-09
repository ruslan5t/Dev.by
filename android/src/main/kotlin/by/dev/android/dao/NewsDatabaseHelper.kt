package by.dev.android.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import by.dev.android.domain.Enclosure
import by.dev.android.domain.News
import java.util.*

class NewsDatabaseHelper(context: Context) {

    private val database: SQLiteDatabase

    init {
        database = DbHelper(context).writableDatabase
    }

    val all: List<News>
        get() {
            val newsList = ArrayList<News>()
            var cursor: Cursor? = null
            try {
                cursor = database.query(DbSchema.NewsTable.NAME, null, null, null, null, null, null)
                while (cursor!!.moveToNext()) {
                    newsList.add(getNews(cursor))
                }
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
            return newsList
        }

    fun findById(id: Long): News {
        return all.first { it.id == id }
    }

    fun save(news: News) {
        var values = getContentValues(news)
        val newsId = database.insert(DbSchema.NewsTable.NAME, null, values)
        for (enclosure in news.enclosures) {
            enclosure.newsId = newsId
            values = getContentValues(enclosure)
            database.insert(DbSchema.EnclosureTable.NAME, null, values)
        }
    }

    private fun getNews(cursor: Cursor): News {
        val news = News()
        news.description = getStringFromCursor(cursor, DbSchema.NewsTable.Cols.DESCRIPTION)
        news.fullText = getStringFromCursor(cursor, DbSchema.NewsTable.Cols.FULL_TEXT)
        news.guid = getStringFromCursor(cursor, DbSchema.NewsTable.Cols.GUID)
        news.id = getLongFromCursor(cursor, DbSchema.NewsTable.Cols._ID)
        news.link = getStringFromCursor(cursor, DbSchema.NewsTable.Cols.LINK)
        news.pubDate = Date(getLongFromCursor(cursor, DbSchema.NewsTable.Cols.PUB_DATE))
        news.title = getStringFromCursor(cursor, DbSchema.NewsTable.Cols.TITLE)
        news.enclosures = getEnclosuresByNewsId(news.id!!)
        return news
    }

    private fun getEnclosuresByNewsId(newsId: Long): MutableList<Enclosure> {
        val enclosures = ArrayList<Enclosure>()
        var cursor: Cursor? = null
        try {
            cursor = database.query(DbSchema.EnclosureTable.NAME, null,
                    DbSchema.EnclosureTable.Cols.NEWS_ID + " = ?",
                    arrayOf(newsId.toString()), null, null, null)
            while (cursor!!.moveToNext()) {
                enclosures.add(getEnclosure(cursor))
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return enclosures
    }

    private fun getEnclosure(cursor: Cursor): Enclosure {
        val enclosure = Enclosure()
        enclosure.id = getLongFromCursor(cursor, DbSchema.EnclosureTable.Cols._ID)
        enclosure.type = getStringFromCursor(cursor, DbSchema.EnclosureTable.Cols.TYPE)
        enclosure.url = getStringFromCursor(cursor, DbSchema.EnclosureTable.Cols.URL)
        enclosure.newsId = getLongFromCursor(cursor, DbSchema.EnclosureTable.Cols.NEWS_ID)
        return enclosure
    }

    private fun getStringFromCursor(cursor: Cursor, attr: String): String {
        return cursor.getString(cursor.getColumnIndex(attr))
    }

    private fun getLongFromCursor(cursor: Cursor, attr: String): Long {
        return cursor.getLong(cursor.getColumnIndex(attr))
    }

    private fun getContentValues(news: News): ContentValues {
        val values = ContentValues()
        values.put(DbSchema.NewsTable.Cols.TITLE, news.title)
        values.put(DbSchema.NewsTable.Cols.DESCRIPTION, news.description)
        values.put(DbSchema.NewsTable.Cols.FULL_TEXT, news.fullText)
        values.put(DbSchema.NewsTable.Cols.GUID, news.guid)
        values.put(DbSchema.NewsTable.Cols.LINK, news.link)
        values.put(DbSchema.NewsTable.Cols.PUB_DATE, news.pubDate!!.time)
        return values
    }


    private fun getContentValues(enclosure: Enclosure): ContentValues {
        val values = ContentValues()
        values.put(DbSchema.EnclosureTable.Cols._ID, enclosure.id)
        values.put(DbSchema.EnclosureTable.Cols.NEWS_ID, enclosure.newsId)
        values.put(DbSchema.EnclosureTable.Cols.TYPE, enclosure.type)
        values.put(DbSchema.EnclosureTable.Cols.URL, enclosure.url)
        return values
    }
}