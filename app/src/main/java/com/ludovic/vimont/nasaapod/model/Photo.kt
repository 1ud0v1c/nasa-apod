package com.ludovic.vimont.nasaapod.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ludovic.vimont.nasaapod.helper.time.TimeHelper
import com.squareup.moshi.Json

/**
 * Result of the NASA APOD API
 * @see: NasaAPI
 */
@Entity
data class Photo(
    val title: String,
    @PrimaryKey
    val date: String,
    val url: String,
    val hdurl: String?,
    val explanation: String,
    @field:Json(name = "media_type")
    val mediaType: String,
    @field:Json(name = "service_version")
    val serviceVersion: String,
    val copyright: String?,
) {
    companion object {
        const val DETAIL_DATE_FORMAT = "dd MMMM yyyy"
        const val IMAGE_MEDIA_TYPE = "image"
        const val VIDEO_MEDIA_TYPE = "video"
        const val YOUTUBE_NAME = "youtube"
        const val VIMEO_NAME = "vimeo"
    }

    var videoThumbnail: String? = null

    fun isMediaImage(): Boolean {
        return mediaType == IMAGE_MEDIA_TYPE
    }

    fun isMediaVideo(): Boolean {
        return mediaType == VIDEO_MEDIA_TYPE
    }

    fun isImageHDValid(): Boolean {
        return isMediaImage() && hdurl != null
    }

    /**
     * After analysis, the API, we can have two different type of media:
     * <ul>
     *     <li>photo: the must simple, we can directly use the image url</li>
     *     <li>video: can be more tricky to recover a thumbnail from the given video url. For certain type
     *     of hosting video, it's possible.</li>
     * </ul>
     * @see: https://stackoverflow.com/questions/8841159/how-to-make-youtube-video-thumbnails-in-android/8842839#8842839
     */
    fun getImageURL(): String {
        if (isMediaVideo()) {
            return videoThumbnail ?: ""
        }
        return url
    }

    fun isYoutubeVideo(): Boolean {
        return isMediaVideo() && url.contains(YOUTUBE_NAME)
    }

    /**
     * Format example: https://www.youtube.com/embed/ictZttw3c98?rel=0
     */
    fun getYoutubeID(): String {
        return url.split("?")[0].split("/").last()
    }

    fun isVimeoVideo(): Boolean {
        return isMediaVideo() && url.contains(VIMEO_NAME)
    }

    /**
     * Format example: https://player.vimeo.com/video/438799770
     */
    fun getVimeoID(): String {
        return url.split("/").last()
    }

    fun getReversedDate(): String {
        return date.split("-").reversed().joinToString("-")
    }

    fun getFormattedDate(): String {
        TimeHelper.getFormattedDate(date)?.let { formattedDate: String ->
            return formattedDate
        }
        return date
    }

    fun getApodLink(): String {
        val fragmentedDate: List<String> = date.split("-")
        val year: String = fragmentedDate[0].substring(2)
        val month: String = fragmentedDate[1]
        val day: String = fragmentedDate[2]
        return "https://apod.nasa.gov/apod/ap$year$month$day.html"
    }

}