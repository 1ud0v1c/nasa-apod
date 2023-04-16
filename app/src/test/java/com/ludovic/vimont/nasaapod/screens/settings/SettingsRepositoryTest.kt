package com.ludovic.vimont.nasaapod.screens.settings

import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsRepositoryTest : KoinTest {
    private val homeRepository: HomeRepository by inject()
    private val settingsRepository: SettingsRepository by inject()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testGetQuota(): Unit = runBlocking {
        Assert.assertEquals(
            NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR,
            settingsRepository.getQuota().split("/")[0].toInt()
        )
        homeRepository.retrievedNasaPhotos().data?.let { _: List<Photo> ->
            Assert.assertNotEquals(
                NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR,
                settingsRepository.getQuota().split("/")[0].toInt()
            )
        }
    }
}