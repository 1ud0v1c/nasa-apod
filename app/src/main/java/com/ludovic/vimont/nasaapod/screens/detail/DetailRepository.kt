package com.ludovic.vimont.nasaapod.screens.detail

import android.graphics.Bitmap
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.FutureTarget
import com.ludovic.vimont.nasaapod.api.glide.GlideDispatchProgressListener
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.ui.BitmapRequestListener
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class DetailRepository: KoinComponent {
    private val photoDao: PhotoDao by inject()
    private val glide: RequestManager by inject()
    private var currentFutureTarget: FutureTarget<Bitmap>? = null

    suspend fun getPhoto(photoId: String): Photo {
        return photoDao.get(photoId)
    }

    fun fetchBitmap(imageURL: String): StateData<Bitmap> {
        var stateData: StateData<Bitmap> = StateData.loading()
        try {
            currentFutureTarget = glide.asBitmap().load(imageURL).listener(BitmapRequestListener {
                stateData = it
                GlideDispatchProgressListener.remove(imageURL)
            }).submit()
            currentFutureTarget?.get()
        } catch (exception: Exception) {
            val errorMessage = "An error occurred while fetching the image. Check if you are connected to internet and retry."
            stateData = StateData.error(errorMessage)
        }
        return stateData
    }

    fun cancelBitmapDownload() {
        currentFutureTarget?.cancel(true)
    }
}