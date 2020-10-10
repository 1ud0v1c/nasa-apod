package com.ludovic.vimont.nasaapod.screens.detail

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    private val detailRepository = DetailRepository()
    val photo = MutableLiveData<Photo>()
    val bitmap = MutableLiveData<Bitmap>()

    fun loadPhoto(photoDate: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val loadedPhoto: Photo = detailRepository.getPhoto(photoDate)
            photo.postValue(loadedPhoto)
        }
    }

    fun loadImageHD(imageURL: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val loadedBitmap: Bitmap = detailRepository.fetchImage(imageURL)
            bitmap.postValue(loadedBitmap)
        }
    }
}