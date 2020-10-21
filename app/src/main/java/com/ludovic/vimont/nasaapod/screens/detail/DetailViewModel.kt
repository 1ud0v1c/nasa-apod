package com.ludovic.vimont.nasaapod.screens.detail

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    private val detailRepository = DetailRepository()
    val photo = MutableLiveData<Photo>()
    val bitmap = MutableLiveData<StateData<Bitmap>>()

    fun loadPhoto(photoDate: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val loadedPhoto: Photo = detailRepository.getPhoto(photoDate)
            photo.postValue(loadedPhoto)
        }
    }

    fun downloadImageHD(imageURL: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (bitmap.value?.status != DataStatus.LOADING) {
                bitmap.postValue(StateData.loading())
                val stateData: StateData<Bitmap> = detailRepository.fetchBitmap(imageURL)
                bitmap.postValue(stateData)
            }
        }
    }
}