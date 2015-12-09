package by.dev.android.view

import android.app.Fragment
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.dev.android.R
import by.dev.android.domain.News
import by.dev.android.service.NewsService

class NewsDetailFragment : Fragment() {

    private var savedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (savedView == null) {
            savedView = inflater.inflate(R.layout.fragment_news_details, container, false)
            var newsService = NewsService(this.activity)
            var news: News = newsService.findById(activity.intent.extras.get("id") as Long)
            (savedView!!.findViewById(R.id.newsTitle) as TextView).text = news.title
            (savedView!!.findViewById(R.id.newsContent) as TextView).text = Html.fromHtml(news.fullText)
        }
        return savedView
    }
}
