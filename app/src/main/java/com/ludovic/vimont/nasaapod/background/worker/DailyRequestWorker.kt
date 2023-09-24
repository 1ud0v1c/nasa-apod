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

class DailyRequestWorker(context: Context,
                         workerParameters: WorkerParameters,
                         private val homeRepository: HomeRepository,
                         private val notificationBuilder: PhotoNotificationBuilder,
                         private val bitmapLoader: BitmapLoader): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val stateDataPhotos: StateData<List<Photo>> = homeRepository.retrievedNasaPhotos(
            isRefreshNeeded = true,
        )
        if (stateDataPhotos.status != DataStatus.SUCCESS) return Result.failure()
        val listOfPhotos = stateDataPhotos.data ?: return Result.failure()

        val newPhoto: Photo = listOfPhotos.first()
        val bitmap: Bitmap = bitmapLoader.loadBitmap(newPhoto.getImageURL())

        notificationBuilder.showNotification(
            photo = newPhoto,
            largeIcon = bitmap,
        )

        return Result.success()
    }

}
