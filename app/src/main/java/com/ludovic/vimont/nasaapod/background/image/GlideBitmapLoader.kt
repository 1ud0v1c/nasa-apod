package com.ludovic.vimont.nasaapod.background.image

import android.graphics.Bitmap
import com.bumptech.glide.RequestManager
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class GlideBitmapLoader: BitmapLoader, KoinComponent {
    private val glide: RequestManager by inject()

    override fun loadBitmap(url: String): Bitmap {
        return glide.asBitmap()
            .load(url)
            .submit()
            .get()
    }
}