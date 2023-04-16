package com.ludovic.vimont.nasaapod.screens.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.preferences.Constants
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationPermissionViewModel(
    private val dataHolder: DataHolder,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel() {
    fun onNotificationPermissionSeen() {
        viewModelScope.launch(dispatcher) {
            dataHolder[Constants.NOTIFICATION_PERMISSION_KEY] = true
        }
    }
}