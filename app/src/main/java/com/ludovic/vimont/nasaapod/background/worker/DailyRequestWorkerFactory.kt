package com.ludovic.vimont.nasaapod.background.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ludovic.vimont.nasaapod.background.image.BitmapLoader
import com.ludovic.vimont.nasaapod.background.image.GlideBitmapLoader
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository

class DailyRequestWorkerFactory(private val homeRepository: HomeRepository,
                                private val bitmapLoader: BitmapLoader): WorkerFactory() {
    /**
     * Return null, so that the base class can delegate to the default WorkerFactory.
     * @see: https://medium.com/androiddevelopers/customizing-workmanager-fundamentals-fdaa17c46dd2
     */
    override fun createWorker(appContext: Context,
                              workerClassName: String,
                              workerParameters: WorkerParameters): ListenableWorker? {
        return when(workerClassName) {
            DailyRequestWorker::class.java.name -> {
                DailyRequestWorker(appContext, workerParameters, homeRepository, bitmapLoader)
            }
            else -> {
                null
            }
        }
    }
}