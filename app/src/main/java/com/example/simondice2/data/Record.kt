package com.example.simondice2.data

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Data class que representa el record: timestamp (ms epoch) + ronda máxima.
 *
 * - Usamos Instant + DateTimeFormatter para formatear la fecha/hora legible.
 * - Referencias:
 *   - Kotlin data classes: https://kotlinlang.org/docs/data-classes.html
 *   - Java Time (Instant): https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html
 */
data class Record(
    val timestampMillis: Long,
    val maxRound: Int
) {
    fun formattedDateTime(zone: ZoneId = ZoneId.systemDefault()): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zone)
        return formatter.format(Instant.ofEpochMilli(timestampMillis))
    }
}
