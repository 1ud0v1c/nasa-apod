package com.ludovic.vimont.nasaapod.preferences

import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class DataHolderTest : KoinTest {
    private val KEY_RANDOM_STRING = "nasa.apod.unit.test"
    private val dataHolder: DataHolder by inject()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testInsert() {
        val defaultString = "default"
        val randomString: String = UUID.randomUUID().toString()
        Assert.assertEquals(defaultString, dataHolder[KEY_RANDOM_STRING, defaultString])
        dataHolder[KEY_RANDOM_STRING] = randomString
        Assert.assertEquals(randomString, dataHolder[KEY_RANDOM_STRING, defaultString])
    }
}