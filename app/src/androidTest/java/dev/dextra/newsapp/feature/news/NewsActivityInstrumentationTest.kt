package dev.dextra.newsapp.feature.news

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.dextra.newsapp.api.model.SourceResponse
import dev.dextra.newsapp.base.BaseInstrumentedTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsActivityInstrumentationTest : BaseInstrumentedTest(){

    val emptyResponse = SourceResponse(ArrayList(), "ok")

    @get:Rule
    val activityRule = ActivityTestRule(NewsActivity::class.java, false, false)

    @Before
    fun setupTest() {
        //we need to lauch the activity here so the MockedEndpointService is set
        activityRule.launchActivity(null)
        Intents.init()
    }

    @After
    fun clearTest() {
        Intents.release()
    }
}