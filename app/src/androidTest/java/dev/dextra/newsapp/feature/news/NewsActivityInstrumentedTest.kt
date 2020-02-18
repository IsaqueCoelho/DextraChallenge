package dev.dextra.newsapp.feature.news

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.dextra.newsapp.R
import dev.dextra.newsapp.TestConstants
import dev.dextra.newsapp.api.model.ArticlesResponse
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseInstrumentedTest
import dev.dextra.newsapp.base.TestSuite
import dev.dextra.newsapp.utils.JsonUtils
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
class NewsActivityInstrumentedTest : BaseInstrumentedTest() {

    private val intent = Intent()
    private lateinit var viewModel: NewsViewModel

    @get:Rule
    val activityRule = ActivityTestRule(NewsActivity::class.java, false, false)

    @Before
    fun setupTest() {
        val source = Source("general",
            "us",
            "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
            "abc-news",
            "en",
            "ABC News",
            "https://abcnews.go.com")

        viewModel = TestSuite.get()

        intent.putExtra(NEWS_ACTIVITY_SOURCE, source)

        //we need to lauch the activity here so the MockedEndpointService is set
        activityRule.launchActivity(intent)
        Intents.init()
    }

    @Test
    fun testNormalState(){
        waitLoading()

        //check if the Sources list is displayed with the correct item and the empty and error states are hidden
        onView(withId(R.id.recyclerview_news)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_state)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testNavigation(){
        waitLoading()

        //check if the Sources list is displayed with the correct item and the empty and error states are hidden
        onView(withId(R.id.button_next_page)).perform(click())
        onView(withId(R.id.button_next_page)).perform(click())
        onView(withId(R.id.button_previous_page)).perform(click())

        onView(withId(R.id.recyclerview_news)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyState(){
        val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)
        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        waitLoading()

        //check if the empty state is displayed with the correct item and the source list and error state are hidden
        onView(withId(R.id.empty_state)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_news)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_state)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testErrorState(){
        TestSuite.mock(TestConstants.newsURL).throwConnectionError().apply()

        waitLoading()

        //check if the empty state is displayed with the correct item and the source list and error state are hidden
        onView(withId(R.id.error_state)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_news)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
    }

    @After
    fun clearTest() {
        Intents.release()
    }

}