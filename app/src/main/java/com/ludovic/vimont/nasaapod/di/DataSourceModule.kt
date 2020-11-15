package com.ludovic.vimont.nasaapod.di

import androidx.room.Room
import com.bumptech.glide.Glide
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.api.RetrofitBuilder
import com.ludovic.vimont.nasaapod.api.VimeoAPI
import com.ludovic.vimont.nasaapod.db.PhotoDatabase
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DataSourceModule {
    val networkModule: Module = module {
        single {
            RetrofitBuilder.buildRetrofitForAPI(NasaAPI.BASE_URL, NasaAPI::class.java)
        }
        single {
            RetrofitBuilder.buildRetrofitForAPI(VimeoAPI.BASE_URL, VimeoAPI::class.java)
        }
    }

    val databaseModule: Module = module {
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

    val glideModule: Module =  module {
        factory {
            Glide.with(androidContext())
        }
        single {
            Glide.get(androidContext())
        }
    }

    val dataHolderModule: Module = module {
        single {
            DataHolder.init(androidContext())
        }
    }
}