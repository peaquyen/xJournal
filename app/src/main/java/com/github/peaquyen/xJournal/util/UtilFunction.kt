package com.github.peaquyen.xJournal.util

import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}

fun Instant.toRealmInstant(): RealmInstant {
    val sec: Long = this.epochSecond
    val nano: Int = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1, -1_000_000 + nano)
    }
}

fun convertLocalDateToInstant(localDate: LocalDate): Instant {
    val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")
    return localDate.atStartOfDay(zoneId).toInstant()
}

fun convertStringToInstant(dateString: String, formatter: DateTimeFormatter): Instant {
    val localDateTime = LocalDateTime.parse(dateString, formatter)
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
}