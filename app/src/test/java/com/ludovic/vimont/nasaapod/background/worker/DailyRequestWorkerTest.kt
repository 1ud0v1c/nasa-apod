package com.ludovic.vimont.nasaapod.background.worker

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.service.notification.StatusBarNotification
import androidx.test.core.app.ApplicationProvider
import androidx.work.testing.TestListenableWorkerBuilder
import com.ludovic.vimont.nasaapod.BitmapHelper
import com.ludovic.vimont.nasaapod.background.image.BitmapLoader
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
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
class DailyRequestWorkerTest : AutoCloseKoinTest() {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val photoDao: PhotoDao by inject()
    private val homeRepository: HomeRepository by inject()
    private lateinit var dailyRequestWorker: DailyRequestWorker

    @Before
    fun setUp() {
        val dailyRequestWorkerFactory =
            DailyRequestWorkerFactory(homeRepository, object : BitmapLoader {
                override fun loadBitmap(url: String): Bitmap {
                    return BitmapHelper.emptyBitmap()
                }
            })

        dailyRequestWorker = TestListenableWorkerBuilder<DailyRequestWorker>(context)
            .setWorkerFactory(dailyRequestWorkerFactory)
            .build() as DailyRequestWorker
    }

    @Test
    fun testDoWork(): Unit = runBlocking {
        Assert.assertTrue(photoDao.getAll().isEmpty())
        dailyRequestWorker.doWork()
        Assert.assertTrue(photoDao.getAll().isNotEmpty())

        val photo: Photo = photoDao.getAll().first()
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val statusBarNotification: StatusBarNotification =
            notificationManager.activeNotifications[0]
        statusBarNotification.notification.extras.getString("android.title")?.let { title: String ->
            Assert.assertEquals(photo.title, title)
        }
        statusBarNotification.notification.extras.getString("android.text")
            ?.let { description: String ->
                Assert.assertEquals(photo.explanation, description)
            }
    }
}