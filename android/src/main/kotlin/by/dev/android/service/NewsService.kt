package by.dev.android.service

import android.content.Context
import android.util.Log
import by.dev.android.dao.NewsDatabaseHelper
import by.dev.android.domain.News
import by.dev.android.util.NewsFeedUtil
import org.xmlpull.v1.XmlPullParserException

import java.io.IOException

class NewsService(context: Context) {

    private val newsDatabaseHelper: NewsDatabaseHelper

    private val newsFeedHelper: NewsFeedUtil

    init {
        newsDatabaseHelper = NewsDatabaseHelper(context)
        newsFeedHelper = NewsFeedUtil()
    }

    val all: List<News>
        get() = newsDatabaseHelper.all

    fun findById(id: Long): News {
        return newsDatabaseHelper.findById(id)
    }

    fun updateDatabase() {
        try {
            val downloadedNewsList = newsFeedHelper.downloadNews()
            val savedNewsList = all
            for (downloadedNewsItem in downloadedNewsList) {
                var alreadySaved = false
                for (savedNewsItem in savedNewsList) {
                    if (savedNewsItem.guid == downloadedNewsItem.guid) {
                        alreadySaved = true
                        break
                    }
                }
                if (!alreadySaved) {
                    newsDatabaseHelper.save(downloadedNewsItem)
                }
            }
        } catch (e: IOException) {
            Log.e(NewsService::class.java.simpleName, "Cannot update database", e)
        } catch (e: XmlPullParserException) {
            Log.e(NewsService::class.java.simpleName, "Cannot update database", e)
        }

    }
}