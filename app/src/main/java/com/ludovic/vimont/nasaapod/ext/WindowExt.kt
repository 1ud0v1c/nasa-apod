package com.ludovic.vimont.nasaapod.ext

import android.view.View
import android.view.Window
import android.view.WindowManager

// https://stackoverflow.com/questions/57094659/how-to-re-show-status-bar-after-exiting-full-screen-navigation-bar-wont-go-aw
/**
 * Use to enable sticky immersive mode: https://developer.android.com/training/system-ui/immersive.
 */
fun Window.useImmersiveActivity() {
    addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
}

/**
 * Reset the navigation
 */
fun Window.resetActivityUIVisibility() {
    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    decorView.systemUiVisibility = View.VISIBLE
}