package com.ludovic.vimont.nasaapod.screens.home

import android.os.Build
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class HomeRepositoryTest: AutoCloseKoinTest() {
    private val photoDao: PhotoDao by inject()
    private lateinit var homeRepository: HomeRepository

    @Before
    fun setUp() {
        homeRepository = HomeRepository()
    }

    @Test
    fun testFetchNasaPhotos() = runBlocking {
        val photos: List<Photo> = homeRepository.fetchNasaPhotos()
        Assert.assertTrue(photos.isNotEmpty())
        Assert.assertEquals(NasaAPI.NUMBER_OF_DAY_TO_FETCH, photos.size)
    }

    @Test
    fun testDatabaseFilled() = runBlocking {
        Assert.assertTrue(photoDao.getAll().isEmpty())

        val photos: List<Photo> = homeRepository.fetchNasaPhotos()
        Assert.assertTrue(photos.isNotEmpty())
        Assert.assertEquals(NasaAPI.NUMBER_OF_DAY_TO_FETCH, photos.size)

        Assert.assertTrue(photoDao.getAll().isNotEmpty())
    }
}