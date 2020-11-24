package com.ludovic.vimont.nasaapod.di

import androidx.room.Room
import com.bumptech.glide.Glide
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.api.RetrofitBuilder
import com.ludovic.vimont.nasaapod.api.VimeoAPI
import com.ludovic.vimont.nasaapod.background.image.GlideBitmapLoader
import com.ludovic.vimont.nasaapod.background.worker.DailyRequestWorkerFactory
import com.ludovic.vimont.nasaapod.db.PhotoDatabase
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import com.ludovic.vimont.nasaapod.screens.detail.DetailRepository
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import com.ludovic.vimont.nasaapod.screens.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DataSourceModule {
    val repositoriesModule: Module = module {
        buildNetworkEntities()
        buildDatabaseEntities()
        buildGlideEntities()
        single {
            DataHolder.init(androidContext())
        }
        buildRepositoriesEntities()
        factory {
            DailyRequestWorkerFactory(get(), get<GlideBitmapLoader>())
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
        factory {
            HomeRepository(get(), get(), get(), get())
        }
        factory {
            DetailRepository(get(), get())
        }
        factory {
            SettingsRepository(get(), get())
        }
    }
}