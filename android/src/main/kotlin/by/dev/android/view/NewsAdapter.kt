package by.dev.android.view

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.dev.android.R
import by.dev.android.domain.News
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(val context: Context, private var newsList: List<News>?) : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.news_item, parent, false)
        return NewsHolder(context, view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val news = newsList!![position]
        holder.bindNews(news)
    }

    override fun getItemCount(): Int {
        return newsList!!.size
    }

    fun setNewsList(newsList: List<News>) {
        this.newsList = newsList
    }

    class NewsHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val itemTitleView: TextView

        private val pubDateView: TextView

        private var news: News? = null

        init {
            itemView.setOnClickListener(this)
            itemTitleView = itemView.findViewById(R.id.newsTitle) as TextView
            pubDateView = itemView.findViewById(R.id.pubDate) as TextView
        }

        fun bindNews(news: News) {
            this.news = news
            itemTitleView.text = news.title
            pubDateView.text = DATE_FORMAT.format(news.pubDate)
        }

        override fun onClick(v: View?) {
            var newsDetailsIntent = Intent(context, NewsDetailActivity::class.java)
            newsDetailsIntent.putExtra("id", news!!.id)
            context.startActivity(newsDetailsIntent)
        }
    }

    companion object {

        private val DATE_FORMAT = SimpleDateFormat("HH:mm dd.MM.yyyy",
                Locale("ru"))
    }
}
