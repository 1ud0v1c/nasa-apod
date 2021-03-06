package com.ludovic.vimont.nasaapod.screens.detail

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ludovic.vimont.nasaapod.api.glide.GlideDispatchProgressListener
import com.ludovic.vimont.nasaapod.api.glide.UIDownloadProgressListener
import com.ludovic.vimont.nasaapod.helper.WallpaperHelper
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val detailRepository: DetailRepository,
                      application: Application): AndroidViewModel(application), UIDownloadProgressListener {
    companion object {
        private const val PICTURES_FOLDERS_NAME = "nasaapod"
    }
    val photo = MutableLiveData<Photo>()
    val bitmap = MutableLiveData<StateData<Bitmap>>()
    val bitmapDownloadProgression = MutableLiveData<Int>()

    fun loadPhoto(photoDate: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val loadedPhoto: Photo = detailRepository.getPhoto(photoDate)
            photo.postValue(loadedPhoto)
        }
    }

    fun setImageAsWallpaper(imageURL: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (bitmap.value?.status != DataStatus.LOADING) {
                bitmap.postValue(StateData.loading())
                GlideDispatchProgressListener.add(imageURL, this@DetailViewModel)
                val stateData: StateData<Bitmap> = detailRepository.fetchBitmap(imageURL)
                bitmap.postValue(stateData)
                if (stateData.status == DataStatus.SUCCESS) {
                    stateData.data?.let { bitmap: Bitmap ->
                        WallpaperHelper.setWallpaper(getApplication(), bitmap)
                    }
                }
            }
        }
    }

    override fun update(downloadProgressionInPercent: Int) {
        bitmapDownloadProgression.postValue(downloadProgressionInPercent)
    }

    fun cancelImageDownload() {
        detailRepository.cancelBitmapDownload()
    }

    fun saveImage(imageURL: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val stateData: StateData<Bitmap> = detailRepository.fetchBitmap(imageURL)
            if (stateData.status == DataStatus.SUCCESS) {
                stateData.data?.let { bitmap: Bitmap ->
                    val photoName = imageURL.split("/").last()
                    detailRepository.saveImage(bitmap, PICTURES_FOLDERS_NAME, photoName)
                }
            }
        }
    }
}