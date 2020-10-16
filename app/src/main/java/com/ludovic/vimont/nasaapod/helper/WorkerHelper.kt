package com.ludovic.vimont.nasaapod.helper

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerHelper {
    inline fun <reified T: CoroutineWorker> launchDailyWorker(context: Context, initialDelayInMs: Long) {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        val dailyRequestWorker: PeriodicWorkRequest = PeriodicWorkRequestBuilder<T>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelayInMs, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(T::class.java.simpleName, ExistingPeriodicWorkPolicy.KEEP, dailyRequestWorker)
    }
}