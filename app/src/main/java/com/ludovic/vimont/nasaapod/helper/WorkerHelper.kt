package com.ludovic.vimont.nasaapod.helper

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import java.util.concurrent.TimeUnit

object WorkerHelper {
    inline fun <reified T : CoroutineWorker> launchDailyWorker(context: Context,
                                                               initialDelayInMs: Long) {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val dailyRequestWorker: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<T>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelayInMs, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                T::class.java.simpleName,
                ExistingPeriodicWorkPolicy.KEEP,
                dailyRequestWorker
            )
    }
}