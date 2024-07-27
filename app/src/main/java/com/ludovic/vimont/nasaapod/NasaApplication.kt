package com.ludovic.vimont.nasaapod

import android.app.Application
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
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
class NasaApplication : Application(), Configuration.Provider {
    companion object {
        private const val WORKER_LAUNCH_TIME: Int = 9
    }

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

        val initialDelay: Long = TimeHelper.computeInitialDelay(WORKER_LAUNCH_TIME)
        WorkerHelper.launchDailyWorker<DailyRequestWorker>(applicationContext, initialDelay)
    }

    override val workManagerConfiguration: Configuration
        get() {
            val delegatingWorkerFactory = DelegatingWorkerFactory()
            delegatingWorkerFactory.addFactory(dailyRequestWorkerFactory)

            return Configuration.Builder()
                .setWorkerFactory(delegatingWorkerFactory)
                .build()
        }

}