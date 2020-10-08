package com.ludovic.vimont.nasaapod.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val homeRepository = HomeRepository()
    val photos = MutableLiveData<List<Photo>>()

    fun loadNasaPhotos() {
        viewModelScope.launch(Dispatchers.Default) {
            val fetchedPhotos: List<Photo> = homeRepository.fetchNasaPhotos()
            photos.postValue(fetchedPhotos)
        }
    }
}