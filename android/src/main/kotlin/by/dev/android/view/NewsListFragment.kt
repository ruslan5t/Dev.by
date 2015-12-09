package by.dev.android.view

import android.app.Fragment
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.dev.android.R
import by.dev.android.service.NewsService

class NewsListFragment : Fragment() {

    private var savedView: View? = null

    private var newsService: NewsService? = null

    private var newsAdapter: NewsAdapter? = null

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (savedView == null) {
            savedView = inflater.inflate(R.layout.fragment_news_list, container, false)
            swipeRefreshLayout = savedView!!.findViewById(R.id.activity_main_swipe_refresh_layout) as SwipeRefreshLayout
            swipeRefreshLayout!!.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                override fun onRefresh() {
                    checkForUpdates()
                }
            })
            recyclerView = savedView!!.findViewById(R.id.news_recycler_view) as RecyclerView
            recyclerView!!.layoutManager = LinearLayoutManager(activity)
            newsAdapter = NewsAdapter(activity, newsService!!.all)
            recyclerView!!.adapter = newsAdapter
            swipeRefreshLayout!!.post(object : Runnable {
                override fun run() {
                    swipeRefreshLayout!!.isRefreshing = true
                }
            })
            checkForUpdates()
        }
        return savedView
    }

    fun setNewsService(newsService: NewsService) {
        this.newsService = newsService
    }

    private fun checkForUpdates() {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                newsService!!.updateDatabase()
                return null
            }

            override fun onPostExecute(aVoid: Void?) {
                val newsList = newsService!!.all
                if (newsList.size > newsAdapter!!.itemCount) {
                    val difference = newsList.size - newsAdapter!!.itemCount
                    newsAdapter!!.setNewsList(newsList)
                    newsAdapter!!.notifyItemRangeInserted(0, difference)
                    recyclerView!!.scrollToPosition(0)
                }
                swipeRefreshLayout!!.isRefreshing = false
            }
        }.execute()
    }
}