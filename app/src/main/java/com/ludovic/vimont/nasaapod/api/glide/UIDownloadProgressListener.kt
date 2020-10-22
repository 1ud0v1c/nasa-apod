package com.ludovic.vimont.nasaapod.api.glide

interface UIDownloadProgressListener {
    fun update(percent: Int)

    fun done()
}