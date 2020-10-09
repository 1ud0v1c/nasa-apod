package com.ludovic.vimont.nasaapod.helper

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

object ViewHelper {
    const val GLIDE_FADE_IN_DURATION = 300

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