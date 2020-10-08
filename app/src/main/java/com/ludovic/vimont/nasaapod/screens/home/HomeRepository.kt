package com.ludovic.vimont.nasaapod.screens.home

import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.helper.TimeHelper
import com.ludovic.vimont.nasaapod.model.Photo
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response

class HomeRepository: KoinComponent {
    private val nasaAPI: NasaAPI by inject()
    private val photoDao: PhotoDao by inject()

    suspend fun fetchNasaPhotos(): List<Photo> {
        val cachedPhotos: List<Photo> = photoDao.getAll()
        if (cachedPhotos.isNotEmpty()) {
            return cachedPhotos
        }
        val photos = ArrayList<Photo>()
        val startDate: String = TimeHelper.getSpecificDay()
        val responsePhoto: Response<List<Photo>> = nasaAPI.getPhotos(startDate)
        responsePhoto.body()?.let { receivedPhotos ->
            val newPhotos: List<Photo> = receivedPhotos.reversed()
            photos.addAll(newPhotos)
            photoDao.insert(newPhotos)
        }
        return photos
    }
}