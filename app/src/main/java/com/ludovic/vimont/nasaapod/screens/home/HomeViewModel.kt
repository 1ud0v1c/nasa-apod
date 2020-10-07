package com.ludovic.vimont.nasaapod.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel(), HomeRepository.HomeRepositoryListener {
    private val homeRepository = HomeRepository()
    val photo = MutableLiveData<Photo>()

    init {
        homeRepository.homeRepositoryListener = this
    }

    fun loadNasaPhotos() {
        viewModelScope.launch(Dispatchers.Default) {
            homeRepository.fetchNasaPhotos()
        }
    }

    override fun onPhotoLoaded(newPhoto: Photo) {
        photo.postValue(newPhoto)
    }
}