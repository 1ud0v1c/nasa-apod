package com.ludovic.vimont.nasaapod.background

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.service.notification.StatusBarNotification
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.BitmapHelper
import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.model.Photo
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class PhotoNotificationBuilderTest : KoinTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val photo: Photo = MockModel.buildPhoto("https://google.fr/test.png")
    private val photoNotificationBuilder = PhotoNotificationBuilder()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testShowNotification() {
        photoNotificationBuilder.showNotification(context, photo, BitmapHelper.emptyBitmap())
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val statusBarNotification: StatusBarNotification =
            notificationManager.activeNotifications[0]
        statusBarNotification.notification.extras.getString("android.title")?.let { title: String ->
            Assert.assertEquals(photo.title, title)
        }
        statusBarNotification.notification.extras.getString("android.text")?.let { description: String ->
            Assert.assertEquals(photo.explanation, description)
        }
    }
}