package com.ludovic.vimont.nasaapod.helper

import com.ludovic.vimont.nasaapod.api.NasaAPI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object TimeHelper {
    /**
     * Return a formatted list of string date based on the current day minus the number of previous
     * days to fetch.
     * For example: today=07/10/2020, numberOfDaysToFetch=3, result=[2020-10-07, 2020-10-06, 2020-10-05]
     */
    fun getLastDays(numberOfDaysToFetch: Int = NasaAPI.NUMBER_OF_DAY_TO_FETCH): List<String> {
        val dates = ArrayList<String>()
        val today = Date()
        val dateFormat = SimpleDateFormat(NasaAPI.API_DATE_FORMAT, Locale.getDefault())
        for (i: Int in numberOfDaysToFetch - 1 downTo 0) {
            val calendar: Calendar = GregorianCalendar()
            calendar.time = today
            calendar.add(Calendar.DAY_OF_MONTH, -i)
            dates.add(dateFormat.format(calendar.time))
        }
        return dates.reversed()
    }
}