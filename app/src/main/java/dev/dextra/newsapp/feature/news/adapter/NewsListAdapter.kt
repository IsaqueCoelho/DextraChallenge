package dev.dextra.newsapp.feature.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import kotlinx.android.synthetic.main.item_news.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsListAdapter(val listener: NewsListAdapterItemListener) :
    RecyclerView.Adapter<NewsListAdapter.NewsListAdapterViewHolder>(){

    private val dataset: ArrayList<Article> = ArrayList()
    private val dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
    private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: NewsListAdapterViewHolder, position: Int) {
        val article = dataset[position]

        holder.view.setOnClickListener { listener.onClick(article) }

        holder.view.article_title.text = article.title
        holder.view.article_description.text = article.description
        holder.view.article_author.text = article.author
        holder.view.article_date.text = dateFormat.format(parseFormat.parse(article.publishedAt))
    }

    fun add(article: List<Article>) {
        dataset.addAll(article)
    }

    fun clear() {
        dataset.clear()
    }

    class NewsListAdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface NewsListAdapterItemListener {
        fun onClick(article: Article)
    }
}