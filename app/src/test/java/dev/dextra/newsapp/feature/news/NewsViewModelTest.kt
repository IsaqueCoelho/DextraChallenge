package dev.dextra.newsapp.feature.news

import dev.dextra.newsapp.TestConstants
import dev.dextra.newsapp.api.model.ArticlesResponse
import dev.dextra.newsapp.base.BaseTest
import dev.dextra.newsapp.base.NetworkState
import dev.dextra.newsapp.base.TestSuite
import dev.dextra.newsapp.utils.JsonUtils
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get

class NewsViewModelTest : BaseTest(){

    lateinit var viewModel: NewsViewModel

    @Before
    fun setupTest() {
        viewModel = TestSuite.get()
    }

    @Test
    fun testGetArticles() {
        viewModel.loadArticlesList(1)

        assert(viewModel.articles.value?.size == 11)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }

    @Test
    fun testEmptyArticles() {
        val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)
        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        viewModel.loadArticlesList(1)

        assert(viewModel.articles.value?.size == 0)
        assertEquals(NetworkState.EMPTY, viewModel.networkState.value)
    }

    @Test
    fun testErrorArticles() {
        TestSuite.mock(TestConstants.newsURL).throwConnectionError().apply()

        viewModel.loadArticlesList(1)

        assert(viewModel.articles.value == null)
        assertEquals(NetworkState.ERROR, viewModel.networkState.value)
    }
}