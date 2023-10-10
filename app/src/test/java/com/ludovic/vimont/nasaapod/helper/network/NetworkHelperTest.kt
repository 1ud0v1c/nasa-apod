package com.ludovic.vimont.nasaapod.helper.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.NetworkMock
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class NetworkHelperTest : KoinTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun `isOnline should return true while the network is available`() {
        // Given
        NetworkMock.mockNetworkAccess(
            context = context,
            shouldBeConnected = true,
        )

        // When
        val isOnline = NetworkHelper.isOnline(context)

        // Then
        assertTrue(isOnline)
    }

    @Test
    fun `isOnline should return false while the network cannot be reached`() {
        // Given
        NetworkMock.mockNetworkAccess(
            context = context,
            shouldBeConnected = false,
        )

        // When
        val isOnline = NetworkHelper.isOnline(context)

        // Then
        assertFalse(isOnline)
    }

}