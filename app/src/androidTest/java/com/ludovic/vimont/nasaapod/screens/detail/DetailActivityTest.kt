package com.ludovic.vimont.nasaapod.screens.detail

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.RecyclerViewItemCountAssertion
import com.ludovic.vimont.nasaapod.screens.home.HomeActivity
import com.ludovic.vimont.nasaapod.screens.zoom.ZoomActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailActivityTest {
    @Rule
    @JvmField
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun detailActivityTestInsideZoomActivity() {
        Intents.init()

        Thread.sleep(3_000)

        onView(withId(R.id.recycler_view_photos)).check(RecyclerViewItemCountAssertion(30))

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view_photos),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        Intents.intended(hasComponent(DetailActivity::class.java.name))

        onView(withId(R.id.image_view_photo)).perform(click())

        Intents.intended(hasComponent(ZoomActivity::class.java.name))

        Intents.release()
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent: ViewParent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}