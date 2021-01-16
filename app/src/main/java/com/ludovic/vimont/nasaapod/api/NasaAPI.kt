package com.ludovic.vimont.nasaapod.api

import com.ludovic.vimont.nasaapod.helper.time.TimeHelper
import com.ludovic.vimont.nasaapod.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This class goal is to request the NASA API called: Astronomy Picture of the Day or APOD.
 * The official documentation don't explain how to request a range of date but this is supported as
 * testimony the github repository: https://github.com/nasa/apod-api#docs-
 * @see: https://api.nasa.gov
 */
interface NasaAPI {
    companion object {
        private const val API_KEY = "jUgv6lDgN4nlkzKZLgruWbzSNOvZLLiisjCc29fz"

        const val BASE_URL = "https://api.nasa.gov/planetary/"
        const val HEADER_RATE_LIMIT = "X-RateLimit-Limit"
        const val HEADER_REMAINING_RATE_LIMIT = "X-RateLimit-Remaining"
        const val DEFAULT_RATE_LIMIT_PER_HOUR = 1_000

        const val NUMBER_OF_DAY_TO_FETCH = 30
        const val API_DATE_FORMAT = "yyyy-MM-dd"
    }

    @GET("apod")
    suspend fun getPhotos(@Query("start_date") startDate: String,
                          @Query("end_date") endDate: String = TimeHelper.getToday(),
                          @Query("hd") hd: Boolean = true,
                          @Query("api_key") apiKey: String = API_KEY): Response<List<Photo>>
}