package com.ludovic.vimont.nasaapod.db

import com.ludovic.vimont.nasaapod.MockModel
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
class PhotoDaoTest : KoinTest {
    private val photos = ArrayList<Photo>()
    private val photoDao: PhotoDao by inject()

    @Before
    fun setUp() {
        photos.add(MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-10"))
        photos.add(MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-09"))
        photos.add(MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-08"))
        photos.add(MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-07"))
    }

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testCount() = runBlocking {
        Assert.assertEquals(0, photoDao.count())
        photoDao.insert(photos)
        Assert.assertEquals(photos.size, photoDao.count())
    }

    @Test
    fun testInsert() = runBlocking {
        Assert.assertEquals(0, photoDao.getAll().size)
        photoDao.insert(photos)
        Assert.assertEquals(photos.size, photoDao.getAll().size)
    }
}