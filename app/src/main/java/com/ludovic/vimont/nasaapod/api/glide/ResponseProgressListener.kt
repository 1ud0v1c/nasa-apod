package com.ludovic.vimont.nasaapod.api.glide

fun interface ResponseProgressListener {

    operator fun invoke(
        url: String,
        totalBytesRead: Long,
        contentLength: Long,
        isDone: Boolean
    )

}