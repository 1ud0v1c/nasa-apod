package com.ludovic.vimont.nasaapod.helper

import com.ludovic.vimont.nasaapod.MockModel
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeRepository
import kotlin.random.Random

class FakeHomeRepository(
    private val photos: List<Photo> = List(Random.nextInt(1, 10)) { MockModel.buildPhoto() },
    private var numberOfDaysToFetch: Int = 30,
): HomeRepository {

    override suspend fun isDatabaseEmpty(): Boolean = photos.isEmpty()

    override suspend fun retrievedNasaPhotos(isRefreshNeeded: Boolean): StateData<List<Photo>> {
        return StateData.success(photos)
    }

    override fun getNumberOfDaysToFetch(): Int = numberOfDaysToFetch

    override fun setNumberOfDaysToFetch(rangeOfDays: Int) {
        this.numberOfDaysToFetch = rangeOfDays
    }

    override fun getCurrentLayout(): String = String()

}