package com.ludovic.vimont.nasaapod.background.image

import android.graphics.Bitmap

fun interface BitmapLoader {
    fun loadBitmap(url: String): Bitmap
}