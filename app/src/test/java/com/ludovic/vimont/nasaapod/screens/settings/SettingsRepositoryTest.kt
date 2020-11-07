package com.ludovic.vimont.nasaapod.screens.settings

import android.os.Build
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SettingsRepositoryTest : AutoCloseKoinTest() {
    private lateinit var homeRepository: HomeRepository
    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setUp() {
        homeRepository = HomeRepository()
        settingsRepository = SettingsRepository()
    }

    @Test
    fun testGetQuota(): Unit = runBlocking {
        Assert.assertEquals(NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR, settingsRepository.getQuota().split("/")[0].toInt())
        homeRepository.retrievedNasaPhotos().data?.let { _: List<Photo> ->
            Assert.assertNotEquals(NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR, settingsRepository.getQuota().split("/")[0].toInt())
        }
    }
}