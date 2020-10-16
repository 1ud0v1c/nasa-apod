package com.ludovic.vimont.nasaapod.background

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import org.koin.core.KoinComponent

class DailyRequestWorker(context: Context,
                         workerParameters: WorkerParameters): CoroutineWorker(context, workerParameters), KoinComponent {
    private val homeRepository = HomeRepository()
    private val notificationBuilder = PhotoNotificationBuilder()

    override suspend fun doWork(): Result {
        val stateDataPhotos: StateData<List<Photo>> = homeRepository.retrievedNasaPhotos(true)
        if (stateDataPhotos.status == DataStatus.SUCCESS) {
            stateDataPhotos.data?.let { photos: List<Photo> ->
                val newPhoto: Photo = photos[0]
                Glide.with(applicationContext)
                    .asBitmap()
                    .load(newPhoto.getImageURL())
                    .into(object: CustomTarget<Bitmap>(){
                        override fun onResourceReady(largeIcon: Bitmap, transition: Transition<in Bitmap>?) {
                            notificationBuilder.showdNotification(applicationContext, newPhoto, largeIcon)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }
        return Result.success()
    }
}