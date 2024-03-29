package com.ludovic.vimont.nasaapod.screens.home

import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.NetworkHelper
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.api.VimeoAPI
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.model.VimeoData
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class HomeRepositoryTest : KoinTest {
    private val vimeoAPI: VimeoAPI by inject()
    private val photoDao: PhotoDao by inject()
    private val homeRepository: HomeRepository by inject()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testRetrievedNasaPhotos(): Unit = runBlocking {
        homeRepository.retrievedNasaPhotos(false).data?.let { photos: List<Photo> ->
            Assert.assertTrue(photos.isNotEmpty())
            Assert.assertEquals(NasaAPI.NUMBER_OF_DAY_TO_FETCH, photos.size)
        }
    }

    @Test
    fun testDatabaseFilled() = runBlocking {
        Assert.assertTrue(photoDao.getAll().isEmpty())
        homeRepository.retrievedNasaPhotos(false).data?.let { photos: List<Photo> ->
            Assert.assertTrue(photos.isNotEmpty())
            Assert.assertEquals(NasaAPI.NUMBER_OF_DAY_TO_FETCH, photos.size)
        }
        Assert.assertTrue(photoDao.getAll().isNotEmpty())
    }

    @Test
    fun testVimeoAPI() = runBlocking {
        val vimeoURL = "https://player.vimeo.com/video/438799770"
        val photo: Photo = MockModel.buildPhoto(vimeoURL, "video")
        val response: Response<List<VimeoData>> = vimeoAPI.getVideoInformation(photo.getVimeoID())
        if (response.isSuccessful) {
            response.body()?.apply {
                val thumbnailURL: String = this[0].thumbnailLarge
                Assert.assertNotNull(thumbnailURL)
                Assert.assertTrue(NetworkHelper.isUrlValid(thumbnailURL))
            }
        }
    }

    @Test
    fun testSetNumberOfDaysToFetch(): Unit = runBlocking {
        val numberOfDays: Int = homeRepository.getNumberOfDaysToFetch()
        Assert.assertEquals(NasaAPI.NUMBER_OF_DAY_TO_FETCH, numberOfDays)
        homeRepository.setNumberOfDaysToFetch(90)
        Assert.assertEquals(90, homeRepository.getNumberOfDaysToFetch())

        homeRepository.retrievedNasaPhotos(false).data?.let { photos: List<Photo> ->
            Assert.assertTrue(photos.isNotEmpty())
            Assert.assertEquals(90, photos.size)
        }
    }
}