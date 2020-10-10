package com.ludovic.vimont.nasaapod.screens.detail

import android.graphics.Bitmap
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.FutureTarget
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.model.Photo
import org.koin.core.KoinComponent
import org.koin.core.inject

class DetailRepository: KoinComponent {
    private val photoDao: PhotoDao by inject()
    private val glide: RequestManager by inject()

    suspend fun getPhoto(photoId: String): Photo {
        return photoDao.get(photoId)
    }

    fun fetchImage(imageURL: String): Bitmap {
        val futureTarget: FutureTarget<Bitmap> = glide.asBitmap().load(imageURL).submit()
        return futureTarget.get()
    }
}