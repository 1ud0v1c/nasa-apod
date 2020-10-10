package com.ludovic.vimont.nasaapod.helper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL


object ViewHelper {
    val TAG: String = ViewHelper::class.java.simpleName
    const val GLIDE_FADE_IN_DURATION = 300

    fun setWallpaper(context: Context, bitmap: Bitmap) {
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || wallpaperManager.isSetWallpaperAllowed) {
            try {
                wallpaperManager.setBitmap(bitmap)
            } catch (e: IllegalArgumentException) {
                Log.d(TAG, "IllegalArgumentException: ${e.message}")
            } finally {
                bitmap.recycle()
            }
        }
    }

    fun fadeInAnimation(view: View, lambda: (() -> Unit)?, duration: Long = 300) {
        fadeAnimation(view, true, duration, lambda)
    }

    fun fadeOutAnimation(view: View, lambda: (() -> Unit)?, duration: Long = 300) {
        fadeAnimation(view, false, duration, lambda)
    }

    private fun fadeAnimation(view: View,
                              isFadeIn: Boolean,
                              duration: Long = 300,
                              lambda: (() -> Unit)?,
                              startOffset: Long = 0) {
        val fadeAnimation: AlphaAnimation = getAlphaAnimation(isFadeIn)
        fadeAnimation.interpolator = AccelerateInterpolator()
        fadeAnimation.duration = duration
        fadeAnimation.startOffset = startOffset
        fadeAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                lambda?.invoke()
            }
        })
        view.startAnimation(fadeAnimation)
    }

    private fun getAlphaAnimation(isFadeIn: Boolean): AlphaAnimation {
        return if (isFadeIn) {
            AlphaAnimation(0f, 1f)
        } else {
            AlphaAnimation(1f, 0f)
        }
    }
}