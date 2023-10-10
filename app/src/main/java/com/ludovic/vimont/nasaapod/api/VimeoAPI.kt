package com.ludovic.vimont.nasaapod.api

import com.ludovic.vimont.nasaapod.model.VimeoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * https://developer.vimeo.com/api/reference/videos#get_video
 */
fun interface VimeoAPI {
    companion object {
        const val BASE_URL = "https://vimeo.com/api/v2/"
    }

    @GET("video/{video_id}.json")
    suspend fun getVideoInformation(@Path("video_id") videoId: String): Response<List<VimeoData>>
}