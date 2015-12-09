package by.dev.android.util

import by.dev.android.domain.Enclosure
import by.dev.android.domain.News
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewsFeedUtil {

    @Throws(IOException::class, XmlPullParserException::class)
    fun downloadNews(): List<News> {
        val inputStream = URL(RSS_FEED_URL).openStream()
        return parseRssFeed(inputStream)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseRssFeed(inputStream: InputStream): List<News> {
        try {
            val parser = XmlPullParserFactory.newInstance().newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return parseRssFeed(parser)
        } finally {
            inputStream.close()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseRssFeed(parser: XmlPullParser): List<News> {
        val newsList = ArrayList<News>()
        var newsItem: News? = null
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name
            if (parser.eventType != XmlPullParser.START_TAG) {
                if (parser.eventType == XmlPullParser.END_TAG && TAG_ITEM == tagName) {
                    newsList.add(newsItem!!)
                    newsItem = null
                }
                continue
            }
            if (TAG_ITEM == tagName) {
                newsItem = News()
                newsItem.enclosures = ArrayList<Enclosure>()
            } else {
                if (newsItem != null) {
                    updateNews(newsItem, tagName, parser)
                }
            }
        }
        return newsList
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun updateNews(news: News, tagName: String, parser: XmlPullParser) {
        if (TAG_ENCLOSURE == tagName) {
            val enclosure = Enclosure()
            if ("url" == parser.getAttributeName(0)) {
                enclosure.url = parser.getAttributeValue(0)
                enclosure.type = parser.getAttributeValue(1)
            } else {
                enclosure.type = parser.getAttributeValue(0)
                enclosure.url = parser.getAttributeValue(1)
            }
            news.enclosures.add(enclosure)
            return
        }
        val value = readTagContent(parser)
        when (tagName) {
            TAG_TITLE -> news.title = value
            TAG_LINK -> news.link = value
            TAG_PUB_DATE -> news.pubDate = parseDate(value)
            TAG_DESCRIPTION -> news.description = value
            TAG_FULL_TEXT -> news.fullText = value
            TAG_GUID -> news.guid = value
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTagContent(parser: XmlPullParser): String {
        parser.next()
        return parser.text
    }

    private fun parseDate(dateStr: String): Date {
        val date: Date
        try {
            date = PUB_DATE_FORMAT.parse(dateStr)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }

        return date
    }

    companion object {

        private val TAG_ITEM = "item"

        private val TAG_TITLE = "title"

        private val TAG_LINK = "link"

        private val TAG_PUB_DATE = "pubDate"

        private val TAG_DESCRIPTION = "description"

        private val TAG_FULL_TEXT = "yandex:full-text"

        private val TAG_GUID = "guid"

        private val TAG_ENCLOSURE = "enclosure"

        private val PUB_DATE_FORMAT = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)

        private val RSS_FEED_URL = "https://dev.by/yandex_rss.xml"
    }
}
