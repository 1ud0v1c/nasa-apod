package com.ludovic.vimont.nasaapod.helper.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData

/**
 * Tools to update the current connectivity status automatically by extending LiveData
 * @see: https://medium.com/@alexzaitsev/android-viewmodel-check-network-connectivity-state-7c028a017cd4
 */
class ConnectionLiveData(private val context: Context): LiveData<Boolean>() {
    companion object {
        val TAG: String = ConnectionLiveData::class.java.simpleName
    }

    private val networkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            postValue(NetworkHelper.isOnline(context))
        }
    }

    override fun onActive() {
        super.onActive()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            val connectivityManager: ConnectivityManager? = context.getSystemService()
            connectivityManager?.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    postValue(true)
                }

                override fun onLost(network: Network) {
                    postValue(false)
                }
            })
        } else {
            context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            try {
                context.unregisterReceiver(networkReceiver)
            } catch (e: Exception) {
                Log.d(TAG, "exception: ${e.message}", e)
            }
        }
    }
}