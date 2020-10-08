package com.ludovic.vimont.nasaapod.model

import android.os.Build
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class PhotoTest: AutoCloseKoinTest() {
    private val imageURL = "https://apod.nasa.gov/apod/image/2010/SquidBat_Akar_960.jpg"
    private val youtubeURLFromAPI = "https://www.youtube.com/embed/DzrCEm1ZBRY?rel=0"
    private val expectedYoutubeThumbnailURL = "https://img.youtube.com/vi/DzrCEm1ZBRY/0.jpg"

    private lateinit var photo: Photo

    @Before
    fun setUp() {
        photo = buildPhoto(imageURL)
    }

    private fun buildPhoto(url: String, mediaType: String = "image"): Photo {
       return Photo("Ou4: A Giant Squid in a Flying Bat", "2020-10-07",
           url, "https://apod.nasa.gov/apod/image/2010/SquidBat_Akar_4485.jpg",
           "A very faint but very large squid-like nebula is visible in planet Earth's sky " +
                   "-- but inside a still larger bat.  The Giant Squid Nebula cataloged as Ou4, and Sh2-129 " +
                   "also known as the Flying Bat Nebula, are both caught in this cosmic scene toward the royal " +
                   "royal constellation Cepheus. Composed with 55 hours of narrowband image data, the telescopic " +
                   "field of view is 3 degrees or 6 Full Moons across. Discovered in 2011 by French astro-imager " +
                   "Nicolas Outters, the Squid Nebula's alluring bipolar shape is distinguished here by the telltale " +
                   "blue-green emission from doubly ionized oxygen atoms. Though apparently completely surrounded by " +
                   "the reddish hydrogen emission region Sh2-129, the true distance and nature of the Squid Nebula have" +
                   " been difficult to determine. Still, a more recent investigation suggests Ou4 really does lie within" +
                   " Sh2-129 some 2,300 light-years away. Consistent with that scenario, Ou4 would represent a spectacular " +
                   "outflow driven by HR8119, a triple system of hot, massive stars seen near the center of the nebula. " +
                   "The truly giant Squid Nebula would physically be nearly 50 light-years across.   " +
                   "New: APOD Mirror in Turkish from Rasyonalist",
           mediaType,"v1", "Yannick Akar")
    }

    @Test
    fun testGetImageURL() {
        Assert.assertEquals(imageURL, photo.getImageURL())
        photo = buildPhoto(youtubeURLFromAPI, "video")
        Assert.assertEquals(expectedYoutubeThumbnailURL, photo.getImageURL())
    }
}