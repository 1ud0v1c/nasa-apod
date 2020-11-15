package com.ludovic.vimont.nasaapod.helper.network

import android.content.Context
import android.os.Build
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.ludovic.vimont.nasaapod.NetworkMock
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class NetworkHelperTest: AutoCloseKoinTest() {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun testIsOnline() {
        shadowOf(getMainLooper()).idle()
        Assert.assertTrue(NetworkHelper.isOnline(context))
        NetworkMock.mockNetworkAccess(context)
        Assert.assertTrue(NetworkHelper.isOnline(context))
    }
}