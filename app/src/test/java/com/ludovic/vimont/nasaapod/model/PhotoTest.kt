package com.ludovic.vimont.nasaapod.model

import android.os.Build
import com.ludovic.vimont.nasaapod.MockModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.net.URL
import java.net.URLConnection

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class PhotoTest : AutoCloseKoinTest() {
    private val imageURL = "https://apod.nasa.gov/apod/image/2010/SquidBat_Akar_960.jpg"
    private val youtubeURLFromAPI = "https://www.youtube.com/embed/DzrCEm1ZBRY?rel=0"
    private val expectedYoutubeThumbnailURL = "https://img.youtube.com/vi/DzrCEm1ZBRY/0.jpg"

    private lateinit var photo: Photo

    @Before
    fun setUp() {
        photo = MockModel.buildPhoto(imageURL)
    }

    @Test
    fun testGetImageURL() {
        Assert.assertEquals(imageURL, photo.getImageURL())
        photo = MockModel.buildPhoto(youtubeURLFromAPI, "video")
        Assert.assertEquals(expectedYoutubeThumbnailURL, photo.getImageURL())
    }

    @Test
    fun testGetApodLink() {
        Assert.assertFalse(isUrlValid("https://www.unit.tests.nasa.apod.com"))
        photo = MockModel.buildPhoto(imageURL, "image", "2020-07-01")
        Assert.assertTrue(isUrlValid(photo.getApodLink()))
    }

    private fun isUrlValid(url: String, timeout: Int = 10_000): Boolean {
        return try {
            val myUrl = URL(url)
            val connection: URLConnection = myUrl.openConnection()
            connection.connectTimeout = timeout
            connection.readTimeout = timeout
            connection.connect()
            true
        } catch (e: Exception) {
            false
        }
    }
}