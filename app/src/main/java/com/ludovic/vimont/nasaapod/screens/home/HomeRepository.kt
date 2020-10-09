package com.ludovic.vimont.nasaapod.screens.home

import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.helper.TimeHelper
import com.ludovic.vimont.nasaapod.model.Photo
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response
import java.lang.Exception

class HomeRepository: KoinComponent {
    private val nasaAPI: NasaAPI by inject()
    private val photoDao: PhotoDao by inject()

    suspend fun isDatabaseEmpty(): Boolean {
        return photoDao.count() <= 0
    }

    suspend fun retrievedNasaPhotos(): StateData<List<Photo>> {
        val cachedPhotos: List<Photo> = photoDao.getAll()
        if (cachedPhotos.isNotEmpty()) {
            return StateData.success(cachedPhotos)
        }
        return fetchNasaPhotos()
    }

    private suspend fun fetchNasaPhotos(): StateData<List<Photo>> {
        val photos = ArrayList<Photo>()
        val startDate: String = TimeHelper.getSpecificDay()
        try {
            val responsePhoto: Response<List<Photo>> = nasaAPI.getPhotos(startDate)
            if (!responsePhoto.isSuccessful) {
                return when (responsePhoto.code()) {
                    429 -> {
                        StateData.error("The user has sent too many requests in a given amount of time.")
                    }
                    else -> {
                        StateData.error(responsePhoto.message())
                    }
                }
            }
            responsePhoto.body()?.let { receivedPhotos ->
                val newPhotos: List<Photo> = receivedPhotos.reversed()
                photos.addAll(newPhotos)
                photoDao.insert(newPhotos)
            }
            return StateData.success(photos)
        } catch (exception: Exception) {
            return StateData.error(exception.message.toString())
        }
    }
}