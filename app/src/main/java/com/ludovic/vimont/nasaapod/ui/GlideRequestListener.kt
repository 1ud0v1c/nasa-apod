package com.ludovic.vimont.nasaapod.ui

import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData

/**
 * Simple overriding of Glide RequestListener which simplify code readability
 */
open class GlideRequestListener(private val lambda: (stateData: StateData<Bitmap>) -> Unit): RequestListener<Bitmap> {
    override fun onResourceReady(resource: Bitmap?,
                                 model: Any?,
                                 target: Target<Bitmap>?,
                                 dataSource: DataSource?,
                                 isFirstResource: Boolean): Boolean {
        lambda(StateData.success(resource))
        return false
    }

    override fun onLoadFailed(exception: GlideException?,
                              model: Any?,
                              target: Target<Bitmap>?,
                              isFirstResource: Boolean): Boolean {
        lambda(StateData.error(exception?.message ?: "An error occurred while fetching the image."))
        return false
    }
}