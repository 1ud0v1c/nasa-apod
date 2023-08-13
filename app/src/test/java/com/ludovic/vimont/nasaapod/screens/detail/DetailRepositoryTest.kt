package com.ludovic.vimont.nasaapod.screens.detail

import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailRepositoryTest : KoinTest {
    private val googleURL = "https://google.fr/"
    private val appleURL = "https://apple.com"
    private val imageMediaType = "image"
    private val videoMediaType = "video"
    private val firstDate = "2020-10-07"
    private val secondDate = "2020-10-08"

    private val photos = ArrayList<Photo>()
    private val photoDao: PhotoDao by inject()
    private val detailRepository: DetailRepository by inject()

    @Before
    fun setUp() {
        photos.add(MockModel.buildPhoto(url = googleURL, mediaType = imageMediaType, date = firstDate))
        photos.add(MockModel.buildPhoto(url = appleURL, mediaType = videoMediaType, date = secondDate))
    }

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testGetPhoto() = runBlocking {
        photoDao.insert(photos)
        val photo: Photo = detailRepository.getPhoto(firstDate)
        Assert.assertEquals(googleURL, photo.url)
        Assert.assertEquals(imageMediaType, photo.mediaType)

        val secondPhoto: Photo = detailRepository.getPhoto(secondDate)
        Assert.assertEquals(appleURL, secondPhoto.url)
        Assert.assertEquals(videoMediaType, secondPhoto.mediaType)
    }
}