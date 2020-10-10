package com.ludovic.vimont.nasaapod.helper

import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.api.NasaAPI.Companion.API_DATE_FORMAT
import com.ludovic.vimont.nasaapod.model.Photo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {
    fun getFormattedDate(dateValue: String,): String? {
        val apiFormat = SimpleDateFormat(NasaAPI.API_DATE_FORMAT, Locale.getDefault())
        try {
            apiFormat.parse(dateValue)?.let { date: Date ->
                val desiredDateFormat = SimpleDateFormat(Photo.DETAIL_DATE_FORMAT, Locale.getDefault())
                return desiredDateFormat.format(date)

            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun getToday(): String = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault()).format(Date().time)

    /**
     * Help us to determine the start_date of the request used in the NasaAPI given an interval of day.
     * We subtract 1 to this interval to exclude the end_date, which is today.
     */
    fun getSpecificDay(numberOfDaysToFetch: Int = NasaAPI.NUMBER_OF_DAY_TO_FETCH): String {
        val today = Date()
        val dateFormat = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault())
        val calendar: Calendar = GregorianCalendar()
        calendar.time = today
        calendar.add(Calendar.DAY_OF_MONTH, -(numberOfDaysToFetch - 1))
        return dateFormat.format(calendar.time)
    }
}