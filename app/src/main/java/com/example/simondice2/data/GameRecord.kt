package com.example.simondice2.data

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class GameRecord(
    val timestampMillis: Long,
    val maxRound: Int,
    val nombreJugador : String
) {
    fun formattedDateTime(zone: ZoneId = ZoneId.systemDefault()): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(zone)
        return formatter.format(Instant.ofEpochMilli(timestampMillis))
    }
}
