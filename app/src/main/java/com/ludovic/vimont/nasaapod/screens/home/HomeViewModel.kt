package com.ludovic.vimont.nasaapod.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val homeRepository = HomeRepository()
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val photosState = MutableLiveData<StateData<List<Photo>>>()

    fun loadNasaPhotos() {
        viewModelScope.launch(Dispatchers.Default) {
            val isConnected: Boolean = isNetworkAvailable.value ?: false
            val isDatabaseEmpty: Boolean = homeRepository.isDatabaseEmpty()

            if (!isConnected && isDatabaseEmpty) {
                photosState.postValue(StateData.error(DataStatus.ERROR_NO_INTERNET, ""))
                return@launch
            }

            if (isDatabaseEmpty) {
                photosState.postValue(StateData.loading())
            }
            val stateData: StateData<List<Photo>> = homeRepository.retrievedNasaPhotos()
            photosState.postValue(stateData)
        }
    }
}