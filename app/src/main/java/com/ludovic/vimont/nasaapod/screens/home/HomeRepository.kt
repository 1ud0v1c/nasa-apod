package com.ludovic.vimont.nasaapod.screens.home

import com.ludovic.vimont.nasaapod.api.RemoteService
import com.ludovic.vimont.nasaapod.helper.TimeHelper
import com.ludovic.vimont.nasaapod.model.Photo
import retrofit2.Response

class HomeRepository {
    suspend fun fetchNasaPhotos(): List<Photo> {
        val photos = ArrayList<Photo>()
        val startDate: String = TimeHelper.getSpecificDay()
        val responsePhoto: Response<List<Photo>> = RemoteService.nasaAPI.getPhotos(startDate)
        responsePhoto.body()?.let { receivedPhotos: List<Photo> ->
            photos.addAll(receivedPhotos.reversed())
        }
        return photos
    }
}