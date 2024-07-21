package com.ludovic.vimont.nasaapod.ui

import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData

open class BitmapRequestListener(private val lambda: (stateData: StateData<Bitmap>) -> Unit): RequestListener<Bitmap> {

    override fun onResourceReady(
        resource: Bitmap,
        model: Any,
        target: Target<Bitmap>?,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        lambda(StateData.success(resource))
        return false
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Bitmap>,
        isFirstResource: Boolean
    ): Boolean {
        lambda(StateData.error(e?.message ?: "An error occurred while fetching the image."))
        return false
    }
}