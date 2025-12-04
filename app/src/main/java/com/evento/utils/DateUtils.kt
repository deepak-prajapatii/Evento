package com.evento.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    /**
     * Converts a timestamp (Long) in milliseconds to a formatted date string.
     * Example output: "Dec 4, 2023"
     */
    fun formatToMonthDayYear(timestampMillis: Long): String {
        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return formatter.format(Date(timestampMillis))
    }

    /**
     * If the timestamp is in seconds instead of milliseconds,
     * convert it and format it correctly.
     */
    fun formatSecondsTimestamp(seconds: Long): String {
        val millis = seconds * 1000L
        return formatToMonthDayYear(millis)
    }
}
