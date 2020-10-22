package com.ludovic.vimont.nasaapod.api.glide

/**
 * The interface used to display the download progression of a Glide request in the UI side.
 */
interface UIDownloadProgressListener {
    fun update(downloadProgressionInPercent: Int)
}