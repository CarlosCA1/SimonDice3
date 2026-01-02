package com.example.simondice2.data

import androidx.room.*

@Dao
interface RecordDao {

    @Query("SELECT * FROM record WHERE id = 1")
    suspend fun getRecord(): RecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: RecordEntity)

    @Query("DELETE FROM record")
    suspend fun deleteRecord()
}
