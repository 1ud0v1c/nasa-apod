package com.ludovic.vimont.nasaapod.extensions

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

internal fun ViewInteraction.isViewDisplayed() {
    this.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}