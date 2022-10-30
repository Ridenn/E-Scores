package com.example.csscorechallenge.utils

import android.annotation.SuppressLint
import com.example.csscorechallenge.extensions.toDate
import java.text.SimpleDateFormat
import java.util.*

object FormatDateUtils {

    private const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd"
    private const val HOUR_MINUTE_FORMAT = "HH:mm"
    private const val CALENDAR_FORMAT = "dd.MM HH:mm"
    private const val WEEK_FORMAT = "E, HH:mm"

    fun convertToReaderReadableDate(readerDate: String): String? {
        val sdfMonthDay = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())

        val resultDate = readerDate.toDate("yyyy-MM-dd'T'HH:mm:ss")
        val current = sdfMonthDay.format(Date())

        return if (current == sdfMonthDay.format(resultDate)) {
            "Hoje, " + simpleDateFormat(HOUR_MINUTE_FORMAT).format(resultDate)
        } else {
            if (getDaysBetweenDates(current, sdfMonthDay.format(resultDate)) > 5) {
                simpleDateFormat(CALENDAR_FORMAT).format(resultDate)
            } else {
                simpleDateFormat(WEEK_FORMAT).format(resultDate)
                    .replace(".", "")
                    .replaceFirstChar { it.titlecase(Locale.getDefault()) }
            }
        }
    }

    private fun simpleDateFormat(format: String): SimpleDateFormat {
        val time = SimpleDateFormat(format, Locale.getDefault())
        time.timeZone = TimeZone.getDefault()
        return time
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDaysBetweenDates(currentDate: String, futureDate: String): Int {
        val format = SimpleDateFormat(SIMPLE_DATE_FORMAT)
        val milliDifference =
            kotlin.math.abs(format.parse(currentDate).time - format.parse(futureDate).time)
        val daysDifference = milliDifference / (24 * 60 * 60 * 1000) //

        return daysDifference.toInt()
    }
}