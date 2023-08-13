package com.ludovic.vimont.nasaapod.background.worker

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.testing.TestListenableWorkerBuilder
import com.ludovic.vimont.nasaapod.BitmapHelper
import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.background.PhotoNotificationBuilder
import com.ludovic.vimont.nasaapod.helper.FakeHomeRepository
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class DailyRequestWorkerTest : KoinTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val photos = listOf(MockModel.buildPhoto(
        explanation = "A very faint but very large squid-like nebula is visible in planet Earth's sky",
    ))
    private val homeRepository: HomeRepository = FakeHomeRepository(photos = photos)
    private val photoNotificationBuilder: PhotoNotificationBuilder by inject()
    private lateinit var dailyRequestWorker: DailyRequestWorker

    @Before
    fun setUp() {
        val dailyRequestWorkerFactory = DailyRequestWorkerFactory(
            homeRepository = homeRepository,
            notificationBuilder = photoNotificationBuilder,
        ) { BitmapHelper.emptyBitmap() }

        dailyRequestWorker = TestListenableWorkerBuilder<DailyRequestWorker>(context)
            .setWorkerFactory(dailyRequestWorkerFactory)
            .build()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testDoWork(): Unit = runBlocking {
        // Given
        val photo = photos.first()

        // When
        dailyRequestWorker.doWork()

        // Then
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val statusBarNotification = notificationManager.activeNotifications[0]
        val notificationTitle = requireNotNull(
            statusBarNotification.notification.extras.getString("android.title")
        )
        val notificationDescription = requireNotNull(
            statusBarNotification.notification.extras.getString("android.text")
        )
        assertEquals(expected = photo.title, actual = notificationTitle)
        assertEquals(expected = photo.explanation, actual = notificationDescription)
    }

}