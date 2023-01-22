package com.ludovic.vimont.nasaapod.screens.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.ludovic.vimont.nasaapod.AppConstants
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.RecyclerViewItemCountAssertion
import com.ludovic.vimont.nasaapod.ViewMatcher
import com.ludovic.vimont.nasaapod.extensions.isViewDisplayed
import com.ludovic.vimont.nasaapod.screens.MainActivity
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private val CONSTRAINT_LAYOUT_CLASS_NAME = "androidx.constraintlayout.widget.ConstraintLayout"

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun homeActivityTestInsideDetailActivity() {
        Thread.sleep(3_000)

        onView(withId(R.id.recycler_view_photos)).check(
            RecyclerViewItemCountAssertion(AppConstants.TOTAL_ITEMS_EXPECTED)
        )

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view_photos),
                ViewMatcher.childAtPosition(
                    withClassName(`is`(CONSTRAINT_LAYOUT_CLASS_NAME)), 0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.image_view_photo)).isViewDisplayed()
        onView(withId(R.id.image_view_media_type)).isViewDisplayed()
        onView(withId(R.id.linear_layout_action_container)).isViewDisplayed()
    }

    @Test
    fun homeActivityTestRefreshAction() {
        Thread.sleep(100)

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.menu_item_refresh), withContentDescription("Refresh"),
                ViewMatcher.childAtPosition(
                    ViewMatcher.childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        Thread.sleep(50)
        onView(withId(R.id.linear_layout_state_container)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view_photos)).check(matches(not(isDisplayed())))

        Thread.sleep(5_000)
        onView(withId(R.id.recycler_view_photos)).check(RecyclerViewItemCountAssertion(AppConstants.TOTAL_ITEMS_EXPECTED))
    }


    @Test
    fun homeActivityTestNumberPickerAction() {
        Thread.sleep(3_000)

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.menu_item_number_picker), withContentDescription("Choose range date"),
                ViewMatcher.childAtPosition(
                    ViewMatcher.childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        Thread.sleep(100)

        onView(withText(R.string.number_picker_title)).check(matches(isDisplayed()))
    }
}