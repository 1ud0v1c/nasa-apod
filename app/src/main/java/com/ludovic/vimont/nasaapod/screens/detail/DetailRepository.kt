package com.ludovic.vimont.nasaapod.screens.detail

import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.model.Photo
import org.koin.core.KoinComponent
import org.koin.core.inject

class DetailRepository: KoinComponent {
    private val photoDao: PhotoDao by inject()

    suspend fun getPhoto(photoId: Int): Photo {
        return photoDao.get(photoId)
    }
}