package com.example.simondice2.data

fun RecordEntity.toDomain(): GameRecord =
    GameRecord(timestampMillis, maxRound)

fun GameRecord.toEntity(): RecordEntity =
    RecordEntity(
        id = 1,
        timestampMillis = timestampMillis,
        maxRound = maxRound
    )
