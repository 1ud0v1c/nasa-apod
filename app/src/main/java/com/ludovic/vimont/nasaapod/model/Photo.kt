package com.ludovic.vimont.nasaapod.model

import com.squareup.moshi.Json

/**
 * Result of the NASA APOD API
 * @see: NasaAPI
 */
data class Photo(val date: String,
				 val copyright: String,
				 @field:Json(name = "media_type")
				 val mediaType: String,
				 val hdurl: String,
				 @field:Json(name = "service_version")
				 val serviceVersion: String,
				 val explanation: String,
				 val title: String,
				 val url: String) {
	companion object {
		const val VIDEO_MEDIA_TYPE = "video"
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
		if (mediaType == VIDEO_MEDIA_TYPE) {
			if (url.contains("youtube")) {
				val youtubeId: String = url.split("?")[0].split("/").last()
				return "https://img.youtube.com/vi/$youtubeId/0.jpg"
			}
		}
		return url
	}
}