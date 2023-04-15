package com.ludovic.vimont.nasaapod.screens

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.helper.MainCoroutineRule
import com.ludovic.vimont.nasaapod.helper.test
import com.ludovic.vimont.nasaapod.screens.MainViewModel.NavigationEvent
import com.ludovic.vimont.nasaapod.preferences.Constants
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
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
    fun `hasNotificationPermissionBeingAsked should send AskForNotificationPermission`() = runTest {
        // Given
        val viewModel = MainViewModel(dataHolder)

        // When
        viewModel.hasNotificationPermissionBeingAsked()
        advanceUntilIdle()

        // Then
        viewModel.navigationEvent.test().assertValue(NavigationEvent.AskForNotificationPermission)
    }

    @Test
    fun `hasNotificationPermissionBeingAsked should do nothing, if permission already asked`() = runTest {
        // Given
        dataHolder[Constants.NOTIFICATION_PERMISSION_KEY] = true
        val viewModel = MainViewModel(dataHolder)

        // When
        viewModel.hasNotificationPermissionBeingAsked()
        advanceUntilIdle()

        // Then
        viewModel.navigationEvent.test().assertNoValue()
    }
}