package com.ludovic.vimont.nasaapod.screens.home

import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo

interface HomeRepository {

    suspend fun isDatabaseEmpty(): Boolean

    suspend fun retrievedNasaPhotos(isRefreshNeeded: Boolean): StateData<List<Photo>>

    fun getNumberOfDaysToFetch(): Int

    fun setNumberOfDaysToFetch(rangeOfDays: Int)

    fun getCurrentLayout(): String

}