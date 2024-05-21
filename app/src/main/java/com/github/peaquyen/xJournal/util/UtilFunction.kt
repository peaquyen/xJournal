package com.github.peaquyen.xJournal.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertStringToInstant(dateString: String, formatter: DateTimeFormatter): Instant {
    val localDateTime = LocalDateTime.parse(dateString, formatter)
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
}

fun String.convertStringToInstant(): Instant {
    val formatter = DateTimeFormatter.ISO_INSTANT
    return Instant.from(formatter.parse(this))
}