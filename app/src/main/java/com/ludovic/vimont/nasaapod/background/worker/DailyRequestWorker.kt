package com.ludovic.vimont.nasaapod.background.worker

import android.content.Context
import android.graphics.Bitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ludovic.vimont.nasaapod.background.PhotoNotificationBuilder
import com.ludovic.vimont.nasaapod.background.image.BitmapLoader
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class DailyRequestWorker(context: Context,
                         workerParameters: WorkerParameters,
                         private val bitmapLoader: BitmapLoader): CoroutineWorker(context, workerParameters), KoinComponent {
    private val homeRepository: HomeRepository by inject()
    private val notificationBuilder = PhotoNotificationBuilder()

    override suspend fun doWork(): Result {
        val stateDataPhotos: StateData<List<Photo>> = homeRepository.retrievedNasaPhotos(true)
        if (stateDataPhotos.status == DataStatus.SUCCESS) {
            stateDataPhotos.data?.let { photos: List<Photo> ->
                val newPhoto: Photo = photos[0]
                val bitmap: Bitmap = bitmapLoader.loadBitmap(newPhoto.getImageURL())
                notificationBuilder.showNotification(applicationContext, newPhoto, bitmap)
            }
            return Result.success()
        }
        return Result.failure()
    }
}