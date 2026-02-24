package com.example.simondice2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class RecordEntity(
    @PrimaryKey val id: Int = 1,
    val timestampMillis: Long,
    val maxRound: Int,
    val nombreJugador : String
)
