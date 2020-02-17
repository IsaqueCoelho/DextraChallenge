package dev.dextra.newsapp.feature.news

import androidx.lifecycle.MutableLiveData
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.api.repository.NewsRepository
import dev.dextra.newsapp.base.BaseViewModel
import dev.dextra.newsapp.base.NetworkState

class NewsViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

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
            newsRepository.getEverything(source?.id, page).subscribe({ response ->
                articles.postValue(response.articles)

                if(response.articles.isEmpty()){
                    networkState.postValue(NetworkState.EMPTY)
                    formState.postValue(true)
                } else {
                    networkState.postValue(NetworkState.SUCCESS)
                    formState.postValue(false)
                }
            },
            {
                formState.postValue(true)
                networkState.postValue(NetworkState.ERROR)
            })
        )
    }
}
