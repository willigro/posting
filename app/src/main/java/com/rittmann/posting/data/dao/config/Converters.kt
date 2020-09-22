package com.rittmann.posting.data.dao.config

import androidx.room.TypeConverter
import com.rittmann.posting.util.DateUtil
import java.util.Calendar

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String): Calendar {
        return DateUtil.parseDate(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Calendar): String {
        return DateUtil.parseDate(date)
    }
}