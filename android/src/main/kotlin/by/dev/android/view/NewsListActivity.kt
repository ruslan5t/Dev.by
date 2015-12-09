package by.dev.android.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import by.dev.android.R
import by.dev.android.service.NewsService

class NewsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_fragment)
        val fm = fragmentManager
        if (fm.findFragmentById(R.id.fragment_container) == null) {
            val newsListFragment = NewsListFragment()
            val newsService = NewsService(this)
            newsListFragment.setNewsService(newsService)
            fm.beginTransaction().add(R.id.fragment_container, newsListFragment).commit()
        }
    }
}