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
import com.ludovic.vimont.nasaapod.helper.TimeHelper
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

        val networkModule: Module = buildNetworkModule()
        val databaseModule: Module = buildDatabaseModule()
        val glideModule: Module = buildGlideModule()
        val dataHolderModule: Module = buildDataHolder()

        startKoin {
            androidContext(this@NasaApplication)
            val listOfModule: List<Module> = listOf(networkModule, databaseModule, glideModule, dataHolderModule)
            modules(listOfModule)
        }

        val initialDelay: Long = TimeHelper.computeInitialDelay(9, 0)
        WorkerHelper.launchDailyWorker<DailyRequestWorker>(applicationContext, initialDelay)
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
            single {
                buildRetrofitForAPI(VimeoAPI.BASE_URL, VimeoAPI::class.java)
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

    private fun buildGlideModule(): Module {
        return module {
            factory {
                Glide.with(androidContext())
            }
        }
    }

    private fun buildDataHolder(): Module {
        return module {
            single {
                DataHolder.init(androidContext())
            }
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val delegatingWorkerFactory = DelegatingWorkerFactory()
        delegatingWorkerFactory.addFactory(DailyRequestWorkerFactory())

        return Configuration.Builder()
            .setWorkerFactory(delegatingWorkerFactory)
            .build()
    }
}