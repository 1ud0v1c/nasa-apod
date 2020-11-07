package com.ludovic.vimont.nasaapod.screens.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {
    private val settingsRepository = SettingsRepository()
    val glideCacheSize = MutableLiveData<Long>()
    val quota = MutableLiveData<String>()

    fun getGlideCacheSize() {
        viewModelScope.launch(Dispatchers.Default) {
            val cacheSize: Long = settingsRepository.getCacheSize()
            glideCacheSize.postValue(cacheSize)
        }
    }

    fun launchClearCache() {
        viewModelScope.launch(Dispatchers.Default) {
            settingsRepository.clearCache()
            getGlideCacheSize()
        }
    }

    fun loadQuota() {
        viewModelScope.launch(Dispatchers.Default) {
            val remainingQuota: String = settingsRepository.getQuota()
            quota.postValue(remainingQuota)
        }
    }
}