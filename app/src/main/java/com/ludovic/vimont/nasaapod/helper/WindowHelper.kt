package com.ludovic.vimont.nasaapod.helper

import android.app.Activity
import android.view.View
import android.view.WindowManager

// https://stackoverflow.com/questions/57094659/how-to-re-show-status-bar-after-exiting-full-screen-navigation-bar-wont-go-aw
object WindowHelper {
    /**
     * Use to enable sticky immersive mode: https://developer.android.com/training/system-ui/immersive.
     */
    fun useImmersiveActivity(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    /**
     * Reset the navigation
     */
    fun resetActivityUIVisibility(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity.window.decorView.systemUiVisibility = View.VISIBLE
    }
}