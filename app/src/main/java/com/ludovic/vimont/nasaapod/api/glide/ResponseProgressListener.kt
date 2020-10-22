package com.ludovic.vimont.nasaapod.api.glide

interface ResponseProgressListener {
    fun update(
        url: String,
        totalBytesRead: Long,
        contentLength: Long,
        isDone: Boolean
    )
}