package com.ludovic.vimont.nasaapod.screens.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    private val detailRepository = DetailRepository()
    val photo = MutableLiveData<Photo>()

    fun loadPhoto(photoId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val loadedPhoto: Photo = detailRepository.getPhoto(photoId)
            photo.postValue(loadedPhoto)
        }
    }
}