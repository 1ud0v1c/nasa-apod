package com.ludovic.vimont.nasaapod

import android.app.Application
import androidx.room.Room
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.db.PhotoDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Suppress("unused")
class NasaApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val networkModule: Module = buildNetworkModule()
        val databaseModule: Module = buildDatabaseModule()

        startKoin {
            androidContext(this@NasaApplication)
            modules(listOf(networkModule, databaseModule))
        }
    }

    private fun buildNetworkModule(): Module {
        return module {
            fun <T> buildRetrofitForAPI(apiURL: String, apiClass: Class<T>): T {
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(apiURL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                return retrofit.create(apiClass)
            }

            single {
                buildRetrofitForAPI(NasaAPI.BASE_URL, NasaAPI::class.java)
            }
        }
    }

    private fun buildDatabaseModule(): Module {
        return module {
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
    }
}