package com.ludovic.vimont.nasaapod.helper

import android.app.Activity
import android.view.View

object WindowHelper {
    /**
     * Use to enable immersive mode: https://developer.android.com/training/system-ui/immersive.
     */
    fun useImmersiveActivity(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}