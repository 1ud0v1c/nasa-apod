package com.ludovic.vimont.nasaapod.helper.time

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
class TimeHelperTest : KoinTest, CalendarInterface {
    private val ONE_HOUR_MS: Int = 60 * 60 * 1_000
    private val ONE_MINUTE_MS: Int = 60 * 1_000
    private var calendarInstance: Calendar = Calendar.getInstance()

    override fun getCalendar(): Calendar = calendarInstance

    @Before
    fun setUp() {
        TimeHelper.calendarInterface = this
    }

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun testGetSpecificDay() {
        val calendar: Calendar = Calendar.getInstance()
        val yesterday: String = TimeHelper.getSpecificDay(-1)
        val yesterdayDay: Int = yesterday.split("-").last().toInt()

        calendar.add(Calendar.DATE, -1)
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH), yesterdayDay)

        val tomorrow: String = TimeHelper.getSpecificDay(1)
        val tomorrowDay: Int = tomorrow.split("-").last().toInt()

        calendar.add(Calendar.DATE, 2)
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH), tomorrowDay)
    }

    @Test
    fun testComputeInitialDelay() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(2020, 10, 23, 14, 0)
        calendarInstance = calendar
        val timeToTriggerWorker: Long = TimeHelper.computeInitialDelay(15)

        // We can lose some seconds while making the test, so we check if we have at most a ONE_MINUTE difference
        Assert.assertEquals(
            timeToTriggerWorker.toDouble(),
            ONE_HOUR_MS.toDouble(),
            ONE_MINUTE_MS.toDouble()
        )

        // Now, we will check, if we compute tomorrow 15h
        val afterDesiredTimeCalendar: Calendar = Calendar.getInstance()
        afterDesiredTimeCalendar.set(2020, 10, 23, 16, 0)
        calendarInstance = afterDesiredTimeCalendar
        val newTimeToTriggerWorker: Long = TimeHelper.computeInitialDelay(15)
        Assert.assertEquals(
            newTimeToTriggerWorker.toDouble(),
            (ONE_HOUR_MS * 23).toDouble(),
            ONE_MINUTE_MS.toDouble()
        )
    }

}