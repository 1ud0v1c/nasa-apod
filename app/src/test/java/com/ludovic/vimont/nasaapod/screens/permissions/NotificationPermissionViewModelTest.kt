package com.ludovic.vimont.nasaapod.screens.permissions

import android.os.Build
import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.helper.MainCoroutineRule
import com.ludovic.vimont.nasaapod.preferences.Constants
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class NotificationPermissionViewModelTest {
    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val dataHolder = DataHolder.init(ApplicationProvider.getApplicationContext())

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun `onNotificationPermissionSeen should save notification in dataHolder`() = runTest {
        // Given
        val viewModel = NotificationPermissionViewModel(dataHolder)

        // When
        viewModel.onNotificationPermissionSeen()
        shadowOf(getMainLooper()).idle()

        // Then
        assertTrue(dataHolder[Constants.NOTIFICATION_PERMISSION_KEY])
    }
}