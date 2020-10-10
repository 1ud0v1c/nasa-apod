package com.ludovic.vimont.nasaapod.model

import com.squareup.moshi.Json

data class VimeoData(
	val id: Int,
	val title: String,
	val description: String,
	val duration: Int,
	val height: Int,
	val width: Int,
	@field:Json(name = "upload_date")
	val uploadDate: String,
	@field:Json(name = "thumbnail_small")
	val thumbnailSmall: String,
	@field:Json(name = "thumbnail_medium")
	val thumbnailMedium: String,
	@field:Json(name = "thumbnail_large")
	val thumbnailLarge: String
)