package com.quinto.comicbook.data.local

import androidx.room.TypeConverter
import java.util.Date


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String): List<Int> {
        if (value.isBlank()) return emptyList()
        return value.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun intListToString(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }
}