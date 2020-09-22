package com.rittmann.posting.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtil {
    const val DEFAULT_FORMAT_BR = "dd/MM/yyyy"

    fun parseDate(string: String, format: String = DEFAULT_FORMAT_BR): Calendar {
        val calendar = Calendar.getInstance()
        try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.isLenient = false
            calendar.time = sdf.parse(string)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return calendar
    }

    fun parseDate(date: Calendar, format: String = DEFAULT_FORMAT_BR): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date.time)
    }

}