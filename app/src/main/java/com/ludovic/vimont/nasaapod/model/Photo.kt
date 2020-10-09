package com.ludovic.vimont.nasaapod.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ludovic.vimont.nasaapod.helper.TimeHelper
import com.squareup.moshi.Json
import java.lang.StringBuilder

/**
 * Result of the NASA APOD API
 * @see: NasaAPI
 */
@Entity
data class Photo(val title: String,
				 val date: String,
				 val url: String,
				 val hdurl: String?,
				 val explanation: String,
				 @field:Json(name = "media_type")
				 val mediaType: String,
				 @field:Json(name = "service_version")
				 val serviceVersion: String,
				 val copyright: String?) {
	companion object {
		const val DETAIL_DATE_FORMAT = "dd MMMM yyyy"
		const val VIDEO_MEDIA_TYPE = "video"
	}

	@PrimaryKey(autoGenerate = true)
	var photoId: Int = 0

	fun isMediaVideo(): Boolean {
		return mediaType == VIDEO_MEDIA_TYPE
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
			if (url.contains("youtube")) {
				val youtubeId: String = url.split("?")[0].split("/").last()
				return "https://img.youtube.com/vi/$youtubeId/0.jpg"
			}
		}
		return url
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