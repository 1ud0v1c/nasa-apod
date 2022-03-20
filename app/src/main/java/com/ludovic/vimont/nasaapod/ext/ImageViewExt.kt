package com.ludovic.vimont.nasaapod.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.helper.ViewHelper

fun ImageView.loadImage(imageURL: String) {
    val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(ViewHelper.GLIDE_FADE_IN_DURATION)
        .setCrossFadeEnabled(true)
        .build()

    val cornersRadiusSize = this.context.resources.getDimensionPixelSize(R.dimen.item_photo_corners_radius)

    Glide.with(this.context)
        .load(imageURL)
        .placeholder(R.drawable.photo_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade(factory))
        .transform(CenterCrop(), RoundedCorners(cornersRadiusSize))
        .into(this)
}