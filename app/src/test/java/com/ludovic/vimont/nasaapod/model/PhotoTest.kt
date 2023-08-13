package com.ludovic.vimont.nasaapod.model

import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.NetworkHelper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PhotoTest : KoinTest {
    private val imageURL = "https://apod.nasa.gov/apod/image/2010/SquidBat_Akar_960.jpg"

    private val youtubeID = "DzrCEm1ZBRY"
    private val youtubeURLFromAPI = "https://www.youtube.com/embed/DzrCEm1ZBRY?rel=0"
    private val expectedYoutubeThumbnailURL = "https://img.youtube.com/vi/DzrCEm1ZBRY/0.jpg"

    private val vimeoID = "438799770"
    private val vimeoURL = "https://player.vimeo.com/video/438799770"

    private lateinit var photo: Photo

    @Before
    fun setUp() {
        photo = MockModel.buildPhoto(url = imageURL)
    }

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testGetImageURL() {
        Assert.assertEquals(imageURL, photo.getImageURL())
        photo = MockModel.buildPhoto(
            url = youtubeURLFromAPI,
            mediaType = "video",
        )
        photo.videoThumbnail = expectedYoutubeThumbnailURL
        Assert.assertEquals(expectedYoutubeThumbnailURL, photo.getImageURL())
    }

    @Test
    fun testGetYoutubeID() {
        photo = MockModel.buildPhoto(
            url = youtubeURLFromAPI,
            mediaType = "video",
        )
        Assert.assertEquals(youtubeID, photo.getYoutubeID())
    }

    @Test
    fun testGetVimeoID() {
        photo = MockModel.buildPhoto(
            url = vimeoURL,
            mediaType = "video",
        )
        Assert.assertEquals(vimeoID, photo.getVimeoID())
    }

    @Test
    fun testGetApodLink() {
        Assert.assertFalse(NetworkHelper.isUrlValid("https://www.unit.tests.nasa.apod.com"))
        photo = MockModel.buildPhoto(
            url = imageURL,
            mediaType = "image",
            date = "2020-07-01",
        )
        Assert.assertTrue(NetworkHelper.isUrlValid(photo.getApodLink()))
    }
}