package com.ludovic.vimont.nasaapod.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.helper.livedata.SingleLiveEvent
import com.ludovic.vimont.nasaapod.preferences.Constants
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataHolder: DataHolder
): ViewModel() {
    private val _navigationEvent = SingleLiveEvent<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    fun hasNotificationPermissionBeingAsked() {
        viewModelScope.launch(Dispatchers.IO) {
            val hasNotificationPermissionBeingAsked = dataHolder[Constants.NOTIFICATION_PERMISSION_KEY, false]
            if (!hasNotificationPermissionBeingAsked) {
                _navigationEvent.postValue(NavigationEvent.AskForNotificationPermission)
            }
        }
    }

    sealed class NavigationEvent {
        object AskForNotificationPermission: NavigationEvent()
    }
}