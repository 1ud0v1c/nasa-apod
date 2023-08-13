package com.ludovic.vimont.nasaapod.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class PhotoDaoTest : KoinTest {
    private val photos = listOf(
        MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-10"),
        MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-09"),
        MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-08"),
        MockModel.buildPhoto(mediaType = Photo.IMAGE_MEDIA_TYPE, date = "2020-10-07"),
    )

    private val db: PhotoDatabase by lazy {
        Room.databaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = PhotoDatabase::class.java,
            name = "NasaApodDb",
        ).fallbackToDestructiveMigration().build()
    }

    private val photoDao: PhotoDao by lazy { db.photoDao() }

    @After
    fun tearDown() {
        db.close()
        GlobalContext.stopKoin()
    }

    @Test
    fun `count should return stored photos size`() = runBlocking {
        // Given
        assertEquals(
            expected = 0,
            actual = photoDao.count(),
        )

        // When
        photoDao.insert(photos)

        // Then
        assertEquals(
            expected = photos.size,
            actual = photoDao.count(),
        )
    }


    @Test
    fun `insert should store the given parameter in database`() = runBlocking {
        // Given
        assertEquals(
            expected = 0,
            actual = photoDao.getAll().size,
        )

        // When
        photoDao.insert(photos)

        // Then
        assertEquals(
            expected = photos.size,
            actual = photoDao.getAll().size,
        )
    }

    @Test
    fun `get should return expected photo entity`() = runBlocking {
        // Given
        val photo = MockModel.buildPhoto()
        photoDao.insert(listOf(photo))

        // When
        val result = photoDao.get(photo.date)

        // Then
        assertEquals(
            expected = photo,
            actual = result,
        )
    }

    @Test
    fun `getAll should return all database data`() = runBlocking {
        // Given
        photoDao.insert(photos)
        assertEquals(
            expected = photos.size,
            actual = photoDao.count(),
        )

        // When
        val result = photoDao.getAll()

        // Then
        assertEquals(
            expected = photos,
            actual = result,
        )
    }

    @Test
    fun `drop should empty the database data`() = runBlocking {
        // Given
        photoDao.insert(photos)
        assertEquals(
            expected = photos.size,
            actual = photoDao.count(),
        )

        // When
        photoDao.drop()

        // Then
        assertEquals(
            expected = 0,
            actual = photoDao.count(),
        )
    }

}