package com.ludovic.vimont.nasaapod.screens.home

import android.util.Log
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.api.VimeoAPI
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.helper.time.TimeHelper
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.model.VimeoData
import com.ludovic.vimont.nasaapod.preferences.DataHolder
import com.ludovic.vimont.nasaapod.preferences.UserPreferences
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response

class HomeRepository : KoinComponent {
    companion object {
        val TAG: String = HomeRepository::class.java.simpleName
    }

    private val nasaAPI: NasaAPI by inject()
    private val vimeoAPI: VimeoAPI by inject()
    private val photoDao: PhotoDao by inject()
    private val dataHolder: DataHolder by inject()

    suspend fun isDatabaseEmpty(): Boolean {
        return photoDao.count() <= 0
    }

    suspend fun retrievedNasaPhotos(isRefreshNeeded: Boolean = false): StateData<List<Photo>> {
        val cachedPhotos: List<Photo> = photoDao.getAll()
        if (cachedPhotos.isNotEmpty() && !isRefreshNeeded) {
            return StateData.success(cachedPhotos)
        }
        return fetchNasaPhotos()
    }

    private suspend fun fetchNasaPhotos(): StateData<List<Photo>> {
        val photos = ArrayList<Photo>()
        val rangeOfDaysToFetch: Int = getNumberOfDaysToFetch() - 1
        val startDate: String = TimeHelper.getSpecificDay(-rangeOfDaysToFetch)
        try {
            val responsePhoto: Response<List<Photo>> = nasaAPI.getPhotos(startDate)
            if (!responsePhoto.isSuccessful) {
                return when (responsePhoto.code()) {
                    429 -> {
                        StateData.error("The user has sent too many requests in a given amount of time.")
                    }
                    else -> {
                        StateData.error(responsePhoto.message())
                    }
                }
            }
            readHeadersResponse(responsePhoto)
            responsePhoto.body()?.let { receivedPhotos: List<Photo> ->
                val newPhotos: List<Photo> = receivedPhotos.reversed()
                updateVideoThumbnails(newPhotos)
                photos.addAll(newPhotos)
                photoDao.drop()
                photoDao.insert(newPhotos)
            }
            return StateData.success(photos)
        } catch (exception: Exception) {
            return StateData.error(exception.message.toString())
        }
    }

    private fun readHeadersResponse(responsePhoto: Response<List<Photo>>) {
        val quotaLimit: Int =
            responsePhoto.headers().get(NasaAPI.HEADER_RATE_LIMIT)?.toIntOrNull()
                ?: NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR
        val remainingQuota: Int =
            responsePhoto.headers().get(NasaAPI.HEADER_REMAINING_RATE_LIMIT)?.toIntOrNull()
                ?: NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR - 1
        dataHolder[UserPreferences.KEY_RATE_LIMIT] = quotaLimit
        dataHolder[UserPreferences.KEY_REMAINING_RATE_LIMIT] = remainingQuota
    }

    /**
     * Allow us to retrieve thumbnail for media of video type.
     */
    private suspend fun updateVideoThumbnails(photos: List<Photo>) {
        for (photo: Photo in photos) {
            if (photo.isYoutubeVideo()) {
                photo.videoThumbnail = "https://img.youtube.com/vi/${photo.getYoutubeID()}/0.jpg"
            }
            if (photo.isVimeoVideo()) {
                try {
                    val vimeoResponse: Response<List<VimeoData>> = vimeoAPI.getVideoInformation(photo.getVimeoID())
                    vimeoResponse.body()?.let { vimeoData: List<VimeoData> ->
                        photo.videoThumbnail = vimeoData[0].thumbnailLarge
                    }
                } catch (exception: Exception) {
                    Log.d(TAG, "Didn't succeed to fetch thumbnail from Vimeo API", exception)
                }
            }
        }
    }

    fun getNumberOfDaysToFetch(): Int {
        return dataHolder[UserPreferences.KEY_RANGE_OF_DAYS_TO_FETCH, NasaAPI.NUMBER_OF_DAY_TO_FETCH]
    }

    fun setNumberOfDaysToFetch(rangeOfDays: Int) {
        dataHolder[UserPreferences.KEY_RANGE_OF_DAYS_TO_FETCH] = rangeOfDays
    }

    fun getCurrentLayout(): String {
        return dataHolder[UserPreferences.KEY_CURRENT_LAYOUT, UserPreferences.DEFAULT_LAYOUT]
    }
}