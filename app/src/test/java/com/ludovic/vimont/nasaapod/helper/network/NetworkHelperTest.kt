package com.ludovic.vimont.nasaapod.helper.network

import android.content.Context
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.NetworkMock
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class NetworkHelperTest : KoinTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testIsOnline() {
        shadowOf(getMainLooper()).idle()
        Assert.assertTrue(NetworkHelper.isOnline(context))
        NetworkMock.mockNetworkAccess(context)
        Assert.assertTrue(NetworkHelper.isOnline(context))
    }
}