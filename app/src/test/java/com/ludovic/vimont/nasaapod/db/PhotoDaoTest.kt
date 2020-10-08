package com.ludovic.vimont.nasaapod.db

import android.os.Build
import com.ludovic.vimont.nasaapod.MockModel
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
class PhotoDaoTest : AutoCloseKoinTest() {
    private val photos = ArrayList<Photo>()
    private val photoDao: PhotoDao by inject()

    @Before
    fun setUp() {
        photos.add(MockModel.buildPhoto("https://google.fr/test.png"))
        photos.add(MockModel.buildPhoto("https://google.fr/test.png"))
        photos.add(MockModel.buildPhoto("https://google.fr/test.png"))
        photos.add(MockModel.buildPhoto("https://google.fr/test.png"))
    }

    @Test
    fun testInsert() = runBlocking {
        Assert.assertEquals(0, photoDao.getAll().size)
        photoDao.insert(photos)
        Assert.assertEquals(photos.size, photoDao.getAll().size)
    }
}