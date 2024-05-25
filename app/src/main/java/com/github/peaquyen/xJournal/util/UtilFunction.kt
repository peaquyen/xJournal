package com.github.peaquyen.xJournal.util

import android.util.Log
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun convertStringToInstant(dateString: String, formatter: DateTimeFormatter): Instant {
    val localDateTime = LocalDateTime.parse(dateString, formatter)
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
}

fun String?.convertStringToInstant(): Instant? {
    if (this.isNullOrEmpty()) {
        return null // Trả về null nếu chuỗi là null hoặc rỗng
    }
    val formatter = DateTimeFormatter.ISO_INSTANT
    return try {
        Instant.from(formatter.parse(this))
    } catch (e: DateTimeParseException) {
        // Xử lý ngoại lệ khi không thể chuyển đổi chuỗi thành Instant
        Log.e("ConvertStringToInstant", "Error parsing string to Instant", e)
        null
    }
}
