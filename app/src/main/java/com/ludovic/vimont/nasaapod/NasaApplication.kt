package com.ludovic.vimont.nasaapod

import android.app.Application
import androidx.work.*
import com.ludovic.vimont.nasaapod.background.worker.DailyRequestWorker
import com.ludovic.vimont.nasaapod.background.worker.DailyRequestWorkerFactory
import com.ludovic.vimont.nasaapod.di.DataSourceModule
import com.ludovic.vimont.nasaapod.di.ViewModelModule
import com.ludovic.vimont.nasaapod.helper.time.TimeHelper
import com.ludovic.vimont.nasaapod.helper.WorkerHelper
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@Suppress("unused")
class NasaApplication: Application(), Configuration.Provider {
    private val dailyRequestWorkerFactory: DailyRequestWorkerFactory by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NasaApplication)
            val listOfModule: List<Module> = listOf(
                DataSourceModule.repositoriesModule,
                ViewModelModule.viewModelModule
            )
            modules(listOfModule)
        }

        val initialDelay: Long = TimeHelper.computeInitialDelay(9, 0)
        WorkerHelper.launchDailyWorker<DailyRequestWorker>(applicationContext, initialDelay)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val delegatingWorkerFactory = DelegatingWorkerFactory()
        delegatingWorkerFactory.addFactory(dailyRequestWorkerFactory)

        return Configuration.Builder()
            .setWorkerFactory(delegatingWorkerFactory)
            .build()
    }
}