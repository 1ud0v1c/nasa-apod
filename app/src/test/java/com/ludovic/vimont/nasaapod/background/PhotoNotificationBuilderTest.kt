package com.ludovic.vimont.nasaapod.background

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.BitmapHelper
import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.model.Photo
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class PhotoNotificationBuilderTest : KoinTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val photoNotificationBuilder: PhotoNotificationBuilder by inject()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testShowNotification() {
        // Given
        val photo: Photo = MockModel.buildPhoto(url = "https://google.fr/test.png")

        // When
        photoNotificationBuilder.showNotification(
            photo = photo,
            largeIcon = BitmapHelper.emptyBitmap(),
        )

        // Then
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val statusBarNotification = notificationManager.activeNotifications[0]

        val title = requireNotNull(
            statusBarNotification.notification.extras.getString("android.title")
        )
        val description = requireNotNull(
            statusBarNotification.notification.extras.getString("android.text")
        )
        assertEquals(
            expected = photo.title,
            actual = title,
        )
        assertEquals(
            expected = photo.explanation,
            actual = description,
        )
    }
}