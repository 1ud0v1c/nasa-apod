package com.ludovic.vimont.nasaapod

import android.app.Application
import androidx.work.*
import com.ludovic.vimont.nasaapod.background.worker.DailyRequestWorker
import com.ludovic.vimont.nasaapod.background.worker.DailyRequestWorkerFactory
import com.ludovic.vimont.nasaapod.di.DataSourceModule
import com.ludovic.vimont.nasaapod.di.ViewModelModule
import com.ludovic.vimont.nasaapod.helper.time.TimeHelper
import com.ludovic.vimont.nasaapod.helper.WorkerHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@Suppress("unused")
class NasaApplication: Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NasaApplication)
            val listOfModule: List<Module> = listOf(
                DataSourceModule.networkModule,
                DataSourceModule.databaseModule,
                DataSourceModule.glideModule,
                DataSourceModule.dataHolderModule,
                ViewModelModule.viewModelModule
            )
            modules(listOfModule)
        }

        val initialDelay: Long = TimeHelper.computeInitialDelay(9, 0)
        WorkerHelper.launchDailyWorker<DailyRequestWorker>(applicationContext, initialDelay)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val delegatingWorkerFactory = DelegatingWorkerFactory()
        delegatingWorkerFactory.addFactory(DailyRequestWorkerFactory())

        return Configuration.Builder()
            .setWorkerFactory(delegatingWorkerFactory)
            .build()
    }
}