package com.example.simondice2.data

class RecordRepository(private val dao: RecordDao) {

    suspend fun getRecord(): RecordEntity? = dao.getRecord()
    suspend fun saveRecord(record: RecordEntity) = dao.insertRecord(record)
    suspend fun deleteRecord() = dao.deleteRecord()
}
