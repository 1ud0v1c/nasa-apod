package com.ludovic.vimont.nasaapod

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowNetwork
import org.robolectric.shadows.ShadowNetworkCapabilities
import org.robolectric.shadows.ShadowNetworkInfo

/**
 * Class to mock network access for unit tests
 */
object NetworkMock {
    private fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun mockNetworkAccess(context: Context, shouldBeConnected: Boolean) {
        val connectivityManager: ConnectivityManager = getConnectivityManager(context)
        val shadowConnectivityManager = Shadows.shadowOf(connectivityManager)

        val networkInfo = if (shouldBeConnected) {
            ShadowNetworkInfo.newInstance(
                /* detailedState = */ NetworkInfo.DetailedState.CONNECTED,
                /* type = */ ConnectivityManager.TYPE_WIFI,
                /* subType = */ 0,
                /* isAvailable = */ false,
                /* state = */ NetworkInfo.State.CONNECTED
            )
        } else {
            ShadowNetworkInfo.newInstance(
                /* detailedState = */ NetworkInfo.DetailedState.DISCONNECTED,
                /* type = */ ConnectivityManager.TYPE_WIFI,
                /* subType = */ 0,
                /* isAvailable = */ false,
                /* state = */ NetworkInfo.State.DISCONNECTED
            )
        }
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo)

        val network = ShadowNetwork.newInstance(1)
        val networkCapabilities: NetworkCapabilities = ShadowNetworkCapabilities.newInstance()
        if (shouldBeConnected) {
            Shadows.shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        }
        shadowConnectivityManager.setNetworkCapabilities(network, networkCapabilities)
    }

}