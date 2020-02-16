package dev.dextra.newsapp.feature.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseListActivity
import dev.dextra.newsapp.feature.news.adapter.NewsListAdapter
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.android.ext.android.inject

const val NEWS_ACTIVITY_SOURCE = "NEWS_ACTIVITY_SOURCE"

class NewsActivity : BaseListActivity(), NewsListAdapter.NewsListAdapterItemListener {

    override val emptyStateTitle: Int = R.string.empty_state_title_news
    override val emptyStateSubTitle: Int = R.string.empty_state_subtitle_news
    override val errorStateTitle: Int = R.string.error_state_title_news
    override val errorStateSubTitle: Int = R.string.error_state_subtitle_news
    override val mainList: View
        get() = recyclerview_news

    private var page: Int = 1

    private val newsViewModel: NewsViewModel by inject()

    private var viewAdapter: NewsListAdapter = NewsListAdapter(this)
    private var viewManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)

    private lateinit var source: Source

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_news)

        (intent?.extras?.getSerializable(NEWS_ACTIVITY_SOURCE) as Source).let { source ->
            title = source.name
            this.source = source
            newsViewModel.configureSource(source)
            loadArticles()
        }

        setupView()

        super.onCreate(savedInstanceState)
    }

    private fun setupView() {
        recyclerview_news.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        button_previous_page.setOnClickListener {
            if(page >= 2){
                page--
                newsViewModel.loadArticlesList(page)
            } else {
                Toast.makeText(this, getString(R.string.error_first_page), Toast.LENGTH_LONG).show()
            }
        }

        button_next_page.setOnClickListener {
            page++
            newsViewModel.loadArticlesList(page)
        }

        newsViewModel.formState.observe(this, Observer {
            stateButtons(it)
        })

    }

    override fun setupPortrait() {
        //not used
    }

    override fun setupLandscape() {
        //not used
    }

    override fun executeRetry() {
        if (source != null){
            loadArticles()
        }
    }

    override fun onClick(article: Article) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(article.url)
        startActivity(intent)
    }

    private fun loadArticles(){

        newsViewModel.articles.observe(this, Observer {

            viewAdapter.apply {
                clear()
                notifyDataSetChanged()
                add(it)
                notifyDataSetChanged()
            }
        })

        newsViewModel.networkState.observe(this, networkStateObserver)

        newsViewModel.loadArticlesList(page)
    }

    private fun stateButtons(state: Boolean) {
        button_previous_page.visibility = if(state) View.GONE else View.VISIBLE
        button_next_page.visibility = if(state) View.GONE else View.VISIBLE
    }

}
