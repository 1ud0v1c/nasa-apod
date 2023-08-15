package com.ludovic.vimont.nasaapod.preferences

import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.util.UUID
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class DataHolderTest : KoinTest {
    private val keyOfRandomString = "nasa.apod.unit.test"
    private val dataHolder: DataHolder by inject()

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun `get should return stored data`() {
        // Given
        val randomString = "lorem ipsum"
        dataHolder[keyOfRandomString] = randomString

        // When
        val result = dataHolder.get<String>(keyOfRandomString)

        // Then
        assertEquals(
            expected = randomString,
            actual = result,
        )
    }

    @Test
    fun `set should put data in SharedPreferences`() {
        // Given
        val defaultString = "default"
        val randomString = UUID.randomUUID().toString()
        val firstString = dataHolder[keyOfRandomString, defaultString]

        // When
        dataHolder[keyOfRandomString] = randomString

        // Then
        assertEquals(
            expected = defaultString,
            actual = firstString,
        )
        assertEquals(
            expected = randomString,
            actual = dataHolder[keyOfRandomString, defaultString],
        )
    }

}