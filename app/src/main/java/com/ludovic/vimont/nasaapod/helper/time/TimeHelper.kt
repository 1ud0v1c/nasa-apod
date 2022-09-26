package com.ludovic.vimont.nasaapod.helper.time

import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.model.Photo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

object TimeHelper {
    var calendarInterface: CalendarInterface = object: CalendarInterface {
        override fun getCalendar(): Calendar {
            return Calendar.getInstance()
        }
    }

    fun getFormattedDate(dateValue: String): String? {
        val apiFormat = SimpleDateFormat(NasaAPI.API_DATE_FORMAT, Locale.getDefault())
        try {
            apiFormat.parse(dateValue)?.let { date: Date ->
                val desiredDateFormat = SimpleDateFormat(
                    Photo.DETAIL_DATE_FORMAT,
                    Locale.getDefault()
                )
                return desiredDateFormat.format(date)

            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun getToday(): String {
        val today = Date()
        val dateFormat = SimpleDateFormat(NasaAPI.API_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(today.time)
    }

    /**
     * Help us to determine the start_date of the request used in the NasaAPI given an interval of day.
     * We subtract 1 to the default interval to exclude the end_date, which is today.
     */
    fun getSpecificDay(numberOfDaysToFetch: Int = (NasaAPI.NUMBER_OF_DAY_TO_FETCH-1)): String {
        val today = Date()
        val dateFormat = SimpleDateFormat(NasaAPI.API_DATE_FORMAT, Locale.getDefault())
        val calendar: Calendar = GregorianCalendar()
        calendar.time = today
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDaysToFetch)
        return dateFormat.format(calendar.time)
    }

    fun computeInitialDelay(hour: Int, minute: Int = 0): Long {
        val calendar: Calendar = calendarInterface.getCalendar()
        val nowMillis: Long = calendar.timeInMillis

        if (calendar[Calendar.HOUR_OF_DAY] > hour ||
            calendar[Calendar.HOUR_OF_DAY] == hour && calendar[Calendar.MINUTE] + 1 >= minute) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        return calendar.timeInMillis - nowMillis
    }
}