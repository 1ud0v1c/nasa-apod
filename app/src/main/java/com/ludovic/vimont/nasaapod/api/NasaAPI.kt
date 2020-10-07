package com.ludovic.vimont.nasaapod.api

import com.ludovic.vimont.nasaapod.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This class goal is to request the NASA API called: Astronomy Picture of the Day or APOD.
 * @see: https://api.nasa.gov
 */
interface NasaAPI {
    companion object {
        private const val API_KEY = "jUgv6lDgN4nlkzKZLgruWbzSNOvZLLiisjCc29fz"

        const val BASE_URL = "https://api.nasa.gov/planetary/"
        const val NUMBER_OF_DAY_TO_FETCH = 30
        const val API_DATE_FORMAT = "yyyy-MM-dd"
    }

    @GET("apod")
    suspend fun getPhoto(@Query("date") date: String,
                         @Query("hd") hd: Boolean = true,
                         @Query("api_key") api_key: String = API_KEY): Response<Photo>
}