package com.example.simondice2

import android.content.ContentValues
import android.content.Context

class ScoreRepository(context: Context) {

    private val dbHelper = ScoreDBHelper(context)

    fun guardarPuntuacion(puntos: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("puntos", puntos)
        }
        db.insert("scores", null, values)
        db.close()
    }

    fun obtenerRecord(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT MAX(puntos) FROM scores", null)

        var record = 0
        if (cursor.moveToFirst()) {
            record = cursor.getInt(0)
        }

        cursor.close()
        db.close()
        return record
    }
}
