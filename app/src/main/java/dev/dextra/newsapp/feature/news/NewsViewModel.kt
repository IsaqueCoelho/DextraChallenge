package dev.dextra.newsapp.feature.news

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.api.repository.NewsRepository
import dev.dextra.newsapp.base.BaseViewModel
import dev.dextra.newsapp.base.NetworkState
import kotlin.math.log

class NewsViewModel(private val newsRepository: NewsRepository,
                    private val context: Context) : BaseViewModel() {

    val articles = MutableLiveData<List<Article>>()
    val networkState = MutableLiveData<NetworkState>()
    val formState = MutableLiveData<Boolean>()

    private var source: Source? = null

    fun configureSource(source: Source) {
        this.source = source
    }

    fun loadArticlesList(page: Int){
        networkState.postValue(NetworkState.RUNNING)
        addDisposable(
            newsRepository.getEverything(source!!.id, page).subscribe({ response ->
                articles.postValue(response.articles)

                if(response.articles.isEmpty()){
                    networkState.postValue(NetworkState.ERROR)
                    formState.postValue(true)
                } else {
                    networkState.postValue(NetworkState.SUCCESS)
                    formState.postValue(false)
                }
            },
            {
                Log.e("NewsActivity", "erro: $it")
                formState.postValue(true)
                networkState.postValue(NetworkState.ERROR)
            })
        )
    }
}
