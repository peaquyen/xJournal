package com.github.peaquyen.xJournal.util

import android.util.Log
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun convertStringToInstant(dateString: String, formatter: DateTimeFormatter): Instant {
    val localDateTime = LocalDateTime.parse(dateString, formatter)
    return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()
}

fun String?.convertStringToInstant(): Instant? {
    if (this.isNullOrEmpty()) {
        return null
    }
    val formatter = DateTimeFormatter.ISO_INSTANT
    return try {
        Instant.from(formatter.parse(this))
    } catch (e: DateTimeParseException) {
        Log.e("ConvertStringToInstant", "Error parsing string to Instant", e)
        null
    }
}

fun getCurrentDateTime() : String {
    val currentDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val formattedCurrentDateTime = currentDateTime.format(formatter)
    return formattedCurrentDateTime
}