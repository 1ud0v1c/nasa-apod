package com.ludovic.vimont.nasaapod.screens.home

import android.os.Build
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class HomeRepositoryTest {
    private val homeRepositoryResult = HomeRepositoryResult()
    private lateinit var homeRepository: HomeRepository

    @Before
    fun setUp() {
        homeRepository = HomeRepository()
        homeRepository.homeRepositoryListener = homeRepositoryResult
    }

    @Test
    fun testFetchNasaPhotos() = runBlocking {
        homeRepository.fetchNasaPhotos()
        Assert.assertTrue(homeRepositoryResult.photos.isNotEmpty())
        Assert.assertEquals(NasaAPI.NUMBER_OF_DAY_TO_FETCH, homeRepositoryResult.photos.size)
    }

    internal class HomeRepositoryResult: HomeRepository.HomeRepositoryListener {
        val photos = ArrayList<Photo>()

        override fun onPhotoLoaded(newPhoto: Photo) {
            photos.add(newPhoto)
        }
    }
}