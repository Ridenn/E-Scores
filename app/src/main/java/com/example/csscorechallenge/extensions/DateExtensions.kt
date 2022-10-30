package com.example.csscorechallenge.extensions

import java.text.SimpleDateFormat
import java.util.*

private const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
private const val UTC_TIME_ZONE = "UTC"

fun String.toDate(
    dateFormat: String = SIMPLE_DATE_FORMAT,
    timeZone: TimeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}