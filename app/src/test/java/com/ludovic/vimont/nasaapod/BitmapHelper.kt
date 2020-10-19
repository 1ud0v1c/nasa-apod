package com.ludovic.vimont.nasaapod

import android.graphics.Bitmap

object BitmapHelper {
    fun emptyBitmap(): Bitmap {
        val conf = Bitmap.Config.ARGB_8888
        return Bitmap.createBitmap(100, 100, conf)
    }
}