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
import retrofit2.Response

class HomeRepositoryImpl(private val nasaAPI: NasaAPI,
                         private val vimeoAPI: VimeoAPI,
                         private val photoDao: PhotoDao,
                         private val dataHolder: DataHolder): HomeRepository {
    companion object {
        private val TAG: String = HomeRepositoryImpl::class.java.simpleName
        private const val HTTP_ERROR_CODE_TOO_MANY_REQUESTS: Int = 429
    }

    override suspend fun isDatabaseEmpty(): Boolean {
        return photoDao.count() <= 0
    }

    override suspend fun retrievedNasaPhotos(isRefreshNeeded: Boolean): StateData<List<Photo>> {
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
                    HTTP_ERROR_CODE_TOO_MANY_REQUESTS -> {
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
        val headers = responsePhoto.headers()
        val quotaLimit: Int = headers.get(NasaAPI.HEADER_RATE_LIMIT)?.toIntOrNull()
                ?: NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR
        val remainingQuota: Int = headers.get(NasaAPI.HEADER_REMAINING_RATE_LIMIT)?.toIntOrNull()
            ?: (NasaAPI.DEFAULT_RATE_LIMIT_PER_HOUR - 1)
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
                    vimeoAPI.getVideoInformation(
                        photo.getVimeoID()
                    ).body()?.let { vimeoData: List<VimeoData> ->
                        photo.videoThumbnail = vimeoData[0].thumbnailLarge
                    }
                } catch (exception: Exception) {
                    Log.d(TAG, "Didn't succeed to fetch thumbnail from Vimeo API", exception)
                }
            }
        }
    }

    override fun getNumberOfDaysToFetch(): Int {
        return dataHolder[UserPreferences.KEY_RANGE_OF_DAYS_TO_FETCH, NasaAPI.NUMBER_OF_DAY_TO_FETCH]
    }

    override fun setNumberOfDaysToFetch(rangeOfDays: Int) {
        dataHolder[UserPreferences.KEY_RANGE_OF_DAYS_TO_FETCH] = rangeOfDays
    }

    override fun getCurrentLayout(): String {
        return dataHolder[UserPreferences.KEY_CURRENT_LAYOUT, UserPreferences.DEFAULT_LAYOUT]
    }
}