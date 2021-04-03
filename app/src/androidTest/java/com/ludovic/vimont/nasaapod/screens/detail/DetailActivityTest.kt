package com.ludovic.vimont.nasaapod.screens.detail

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.ludovic.vimont.nasaapod.AppConstants
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.RecyclerViewItemCountAssertion
import com.ludovic.vimont.nasaapod.screens.MainActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun detailActivityTestInsideZoomActivity() {
        Intents.init()

        Thread.sleep(3_000)

        val recyclerView = onView(withId(R.id.recycler_view_photos))
        recyclerView.check(RecyclerViewItemCountAssertion(AppConstants.TOTAL_ITEMS_EXPECTED))
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        BaristaVisibilityAssertions.assertDisplayed(R.id.image_view_photo)
        BaristaVisibilityAssertions.assertDisplayed(R.id.image_view_media_type)
        BaristaVisibilityAssertions.assertDisplayed(R.id.linear_layout_action_container)

        onView(withId(R.id.image_view_photo)).perform(click())

        BaristaVisibilityAssertions.assertDisplayed(R.id.constraint_layout_zoom_container)
    }
}