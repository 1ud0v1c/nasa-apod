package com.ludovic.vimont.nasaapod.helper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log

object WallpaperHelper {
    private val TAG: String = ViewHelper::class.java.simpleName

    fun setWallpaper(context: Context, bitmap: Bitmap) {
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || wallpaperManager.isSetWallpaperAllowed) {
            try {
                wallpaperManager.setBitmap(bitmap)
            } catch (e: IllegalArgumentException) {
                Log.d(TAG, "IllegalArgumentException: ${e.message}")
            }
        }
    }
}