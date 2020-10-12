package com.ludovic.vimont.nasaapod.preferences

import android.os.Build
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DataHolderTest: AutoCloseKoinTest() {
    private val KEY_RANDOM_STRING = "nasa.apod.unit.test"
    private val dataHolder: DataHolder by inject()

    @Test
    fun testInsert() {
        val defaultString = "default"
        val randomString: String = UUID.randomUUID().toString()
        Assert.assertEquals(defaultString, dataHolder[KEY_RANDOM_STRING, defaultString])
        dataHolder[KEY_RANDOM_STRING] = randomString
        Assert.assertEquals(randomString, dataHolder[KEY_RANDOM_STRING, defaultString])
    }
}