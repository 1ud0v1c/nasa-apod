package com.ludovic.vimont.nasaapod.background.image

import android.graphics.Bitmap
import com.bumptech.glide.RequestManager
import com.ludovic.vimont.nasaapod.background.image.BitmapLoader
import org.koin.core.KoinComponent
import org.koin.core.inject

class GlideBitmapLoader: BitmapLoader, KoinComponent {
    private val glide: RequestManager by inject()

    override fun loadBitmap(url: String): Bitmap {
        return glide.asBitmap()
            .load(url)
            .submit()
            .get()
    }
}