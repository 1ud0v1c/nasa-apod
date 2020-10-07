package com.ludovic.vimont.nasaapod.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RemoteService {
    val nasaAPI: NasaAPI = buildRetrofitForAPI(NasaAPI.BASE_URL, NasaAPI::class.java)

    private fun <T> buildRetrofitForAPI(apiURL: String, apiClass: Class<T>): T {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(apiURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(apiClass)
    }
}