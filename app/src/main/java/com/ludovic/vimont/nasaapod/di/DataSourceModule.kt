package com.ludovic.vimont.nasaapod.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.api.RetrofitBuilder
import com.ludovic.vimont.nasaapod.api.VimeoAPI
import com.ludovic.vimont.nasaapod.background.PhotoNotificationBuilder
import com.ludovic.vimont.nasaapod.background.image.GlideBitmapLoader
import com.ludovic.vimont.nasaapod.background.worker.DailyRequestWorkerFactory
import com.ludovic.vimont.nasaapod.db.PhotoDatabase
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import com.ludovic.vimont.nasaapod.screens.detail.DetailRepository
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import com.ludovic.vimont.nasaapod.screens.home.HomeRepositoryImpl
import com.ludovic.vimont.nasaapod.screens.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DataSourceModule {
    val repositoriesModule: Module = module {
        buildAndroidEntities()
        buildDatabaseEntities()
        buildGlideEntities()
        buildNetworkEntities()
        buildRepositoriesEntities()
        buildWorkers()
    }

    private fun Module.buildAndroidEntities() {
        single {
            androidContext().contentResolver
        }
        single {
            DataHolder(PreferenceManager.getDefaultSharedPreferences(androidContext()))
        }
        single {
            androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        single {
            NotificationCompat.Builder(androidContext(), PhotoNotificationBuilder.CHANNEL_ID)
        }
        single {
            NavDeepLinkBuilder(androidContext())
        }
    }

    private fun Module.buildDatabaseEntities() {
        single {
            val databaseName: String = PhotoDatabase.DATABASE_NAME
            Room.databaseBuilder(androidContext(), PhotoDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
        }
        single {
            get<PhotoDatabase>().photoDao()
        }
    }

    private fun Module.buildNetworkEntities() {
        single {
            RetrofitBuilder.buildRetrofitForAPI(NasaAPI.BASE_URL, NasaAPI::class.java)
        }
        single {
            RetrofitBuilder.buildRetrofitForAPI(VimeoAPI.BASE_URL, VimeoAPI::class.java)
        }
    }

    private fun Module.buildGlideEntities() {
        factory {
            Glide.with(androidContext())
        }
        single {
            Glide.get(androidContext())
        }
        factory {
            GlideBitmapLoader(get())
        }
    }

    private fun Module.buildRepositoriesEntities() {
        factory<HomeRepository> {
            HomeRepositoryImpl(get(), get(), get(), get())
        }
        factory {
            DetailRepository(get(), get(), get())
        }
        factory {
            SettingsRepository(get(), get())
        }
    }

    private fun Module.buildWorkers() {
        single {
            PhotoNotificationBuilder(get(), get(), get())
        }
        factory {
            DailyRequestWorkerFactory(get(), get(), get<GlideBitmapLoader>())
        }
    }
}