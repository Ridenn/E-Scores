package com.example.csscorechallenge.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object FormatDateUtils {

    private const val DEFAULT_PATTERN_SIMPLE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    private const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd"
    private const val HOUR_MINUTE_FORMAT = "HH:mm"
    private const val CALENDAR_FORMAT = "dd.MM HH:mm"
    private const val WEEK_FORMAT = "E, HH:mm"

    fun convertToReaderReadableDate(readerDate: String): String? {
        val sdfMonthDay = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        val current = sdfMonthDay.format(Date())
        val resultDate = Date(toCalendar(readerDate).timeInMillis)

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
        return SimpleDateFormat(format, Locale.getDefault())
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDaysBetweenDates(currentDate: String, futureDate: String): Int {
        val format = SimpleDateFormat(SIMPLE_DATE_FORMAT)
        val milliDifference =
            kotlin.math.abs(format.parse(currentDate).time - format.parse(futureDate).time)
        val daysDifference = milliDifference / (24 * 60 * 60 * 1000) //

        return daysDifference.toInt()
    }

    @SuppressLint("SimpleDateFormat")
    fun toCalendar(dateString: String): Calendar {
        val calendar = GregorianCalendar.getInstance()
        val newDate = dateString.replace("Z", "")
        val date = SimpleDateFormat(DEFAULT_PATTERN_SIMPLE_DATE_FORMAT).parse(newDate)
        date?.let {
            calendar.time = it
        }

        return calendar
    }

}