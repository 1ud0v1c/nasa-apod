package com.ludovic.vimont.nasaapod

import android.app.Application
import androidx.room.Room
import androidx.work.*
import com.bumptech.glide.Glide
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.api.VimeoAPI
import com.ludovic.vimont.nasaapod.background.DailyRequestWorker
import com.ludovic.vimont.nasaapod.background.DailyRequestWorkerFactory
import com.ludovic.vimont.nasaapod.db.PhotoDatabase
import com.ludovic.vimont.nasaapod.di.DataSourceModule
import com.ludovic.vimont.nasaapod.di.ViewModelModule
import com.ludovic.vimont.nasaapod.helper.time.TimeHelper
import com.ludovic.vimont.nasaapod.helper.WorkerHelper
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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