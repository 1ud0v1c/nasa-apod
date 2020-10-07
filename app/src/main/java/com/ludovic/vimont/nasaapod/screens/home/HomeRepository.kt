package com.ludovic.vimont.nasaapod.screens.home

import com.ludovic.vimont.nasaapod.api.RemoteService
import com.ludovic.vimont.nasaapod.helper.TimeHelper
import com.ludovic.vimont.nasaapod.model.Photo
import retrofit2.Response

class HomeRepository {
    interface HomeRepositoryListener {
        fun onPhotoLoaded(newPhoto: Photo)
    }
    var homeRepositoryListener: HomeRepositoryListener? = null

    suspend fun fetchNasaPhotos() {
        val thirtyDays: List<String> = TimeHelper.getLastDays()
        for (currentDay: String in thirtyDays) {
            val responsePhoto: Response<Photo> = RemoteService.nasaAPI.getPhoto(currentDay)
            responsePhoto.body()?.let { photo: Photo ->
                homeRepositoryListener?.onPhotoLoaded(photo)
            }
        }
    }
}