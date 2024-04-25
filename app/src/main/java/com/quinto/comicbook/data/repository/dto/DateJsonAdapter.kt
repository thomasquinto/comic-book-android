package com.quinto.comicbook.data.repository.dto

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateJsonAdapter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    @FromJson
    fun fromJson(json: String): Date? {
        return dateFormat.parse(json.replace("Z", "+0000"))
    }

    @ToJson
    fun toJson(date: Date): String {
        return dateFormat.format(date)
    }
}