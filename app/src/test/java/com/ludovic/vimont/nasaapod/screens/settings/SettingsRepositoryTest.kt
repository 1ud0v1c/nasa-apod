package com.ludovic.vimont.nasaapod.screens.settings

import android.os.Build
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SettingsRepositoryTest : AutoCloseKoinTest() {
    private val homeRepository: HomeRepository by inject()
    private val settingsRepository: SettingsRepository by inject()

    @Test
    fun testGetQuota(): Unit = runBlocking {
        Assert.assertEquals(NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR, settingsRepository.getQuota().split("/")[0].toInt())
        homeRepository.retrievedNasaPhotos().data?.let { _: List<Photo> ->
            Assert.assertNotEquals(NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR, settingsRepository.getQuota().split("/")[0].toInt())
        }
    }
}