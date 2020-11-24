package com.ludovic.vimont.nasaapod.background.image

import android.graphics.Bitmap
import com.bumptech.glide.RequestManager

class GlideBitmapLoader(private val glide: RequestManager): BitmapLoader {
    override fun loadBitmap(url: String): Bitmap {
        return glide.asBitmap()
            .load(url)
            .submit()
            .get()
    }
}