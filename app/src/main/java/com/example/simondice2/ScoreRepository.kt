package com.example.simondice2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class ScoreRepository(context: Context) {

    private val dbHelper = ScoreDBHelper(context)

    fun guardarPuntuacion(puntos: Int) {
        var db: SQLiteDatabase? = null
        try{
            db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("puntos", puntos)
            put("fecha", System.currentTimeMillis())
        }
        db.insert("scores", null, values)

        // Mantener solo los 10 mejores
        // Borro los registros que no estén en el TOP 10 (ordenados por puntos DESC, fecha ASC)
        val deleteQuery = """
        DELETE FROM scores 
        WHERE id NOT IN (
            SELECT id FROM scores 
            ORDER BY puntos DESC, fecha ASC 
            LIMIT 10
        )
    """
        db.execSQL(deleteQuery)
    } catch (e: Exception) {
        Log.e("ScoreRepository", "Error al guardar: ${e.message}")
    } finally {
        // Esto garantiza que la conexión se cierre siempre
        db?.close()
    }
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
    /**
     * Comprueba si una puntuación entraría dentro de los 10 mejores.
     * Útil para lanzar el mensaje al Logcat antes o después de guardar.
     */
    fun esTopDiez(puntos: Int): Boolean {
        val db = dbHelper.readableDatabase

        // Contamos cuántos registros hay
        val countCursor = db.rawQuery("SELECT COUNT(*) FROM scores", null)
        countCursor.moveToFirst()
        val numRegistros = countCursor.getInt(0)
        countCursor.close()

        // Si hay menos de 10, cualquier puntuación entra
        if (numRegistros < 10) {
            db.close()
            return true
        }

        // Si hay 10 o más, comparamos con la puntuación más baja del Top 10
        val cursor = db.rawQuery("""
            SELECT MIN(puntos) FROM (
                SELECT puntos FROM scores 
                ORDER BY puntos DESC, fecha ASC 
                LIMIT 10
            )
        """.trimIndent(), null)

        // Buscamos la puntuación más baja del Top 10 actual
        // Si el cursor tiene datos (moveToFirst), comparamos la puntuación obtenida
        // con ese mínimo. Si es igual o mayor, el jugador entra en el ranking
        var esTop = false
        if (cursor.moveToFirst()) {
            val minimaPuntuacionTop = cursor.getInt(0)
            if (puntos >= minimaPuntuacionTop) {
                esTop = true
            }
        }

        cursor.close()
        db.close()
        return esTop
    }
}