package com.ludovic.vimont.nasaapod.helper

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

object ViewHelper {
    const val GLIDE_FADE_IN_DURATION = 300

    fun fadeInAnimation(view: View, onAnimationEndLambda: ((View) -> Unit)?, duration: Long = 300) {
        fadeAnimation(view, true, duration, onAnimationEndLambda)
    }

    fun fadeOutAnimation(view: View, onAnimationEndLambda: ((View) -> Unit)?, duration: Long = 300) {
        fadeAnimation(view, false, duration, onAnimationEndLambda)
    }

    private fun fadeAnimation(view: View,
                              isFadeIn: Boolean,
                              duration: Long = 300,
                              onAnimationEndLambda: ((View) -> Unit)?,
                              startOffset: Long = 0) {
        this.fadeAnimation(
            view = view,
            isFadeIn = isFadeIn,
            duration = duration,
            onAnimationStart = null,
            onAnimationRepeat = null,
            onAnimationEnd = onAnimationEndLambda,
            startOffset = startOffset
        )
    }

    private fun fadeAnimation(view: View,
                              isFadeIn: Boolean,
                              duration: Long = 300,
                              onAnimationStart: ((View) -> Unit)?,
                              onAnimationRepeat: ((View) -> Unit)?,
                              onAnimationEnd: ((View) -> Unit)?,
                              startOffset: Long = 0) {
        val fadeAnimation: AlphaAnimation = getAlphaAnimation(isFadeIn)
        fadeAnimation.interpolator = AccelerateInterpolator()
        fadeAnimation.duration = duration
        fadeAnimation.startOffset = startOffset
        fadeAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                onAnimationStart?.invoke(view)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                onAnimationRepeat?.invoke(view)
            }

            override fun onAnimationEnd(animation: Animation?) {
                onAnimationEnd?.invoke(view)
            }
        })
        view.startAnimation(fadeAnimation)
    }

    private fun getAlphaAnimation(isFadeIn: Boolean): AlphaAnimation {
        return if (isFadeIn) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
    }
}