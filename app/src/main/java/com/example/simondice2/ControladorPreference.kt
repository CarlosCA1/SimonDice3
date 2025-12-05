package com.example.simondice2

import android.content.Context
import androidx.core.content.edit
import com.example.simondice2.data.Record

/**
 * ControladorPreferences: acceso a SharedPreferences (singleton) siguiendo el proyecto "Preferencias".
 *
 * Guarda dos claves:
 *  - KEY_RECORD_ROUND (Int) -> ronda máxima
 *  - KEY_RECORD_TIMESTAMP (Long) -> instante en milis en que se consiguió
 *
 * Comentarios y referencias:
 * - SharedPreferences (Android): https://developer.android.com/training/data-storage/shared-preferences
 * - androidx.core.content.edit extension: https://developer.android.com/jetpack/androidx/releases/core
 *
 * Diseñado para ser simple (sigue el patrón del proyecto subido). Si más adelante quieres
 * migrar a Room, podemos introducir un RecordRepository que delegue a este controlador.
 */
object ControladorPreference {
    private const val PREFS_NAME = "simondice_record_prefs"
    private const val KEY_RECORD_ROUND = "record_round"
    private const val KEY_RECORD_TIMESTAMP = "record_timestamp"

    /**
     * Guarda/actualiza el record (ronda + timestamp) en SharedPreferences.
     *
     * - Usamos edit{} de Android KTX para aplicar los cambios.
     * - Se ejecuta en el hilo que invoque la función; para no bloquear la UI, llama desde un dispatcher IO (ViewModel hace esto).
     *
     * Referencia edit{}: https://developer.android.com/kotlin/ktx
     */
    fun actualizarRecord(context: Context, nuevoRecord: Record) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putInt(KEY_RECORD_ROUND, nuevoRecord.maxRound)
            putLong(KEY_RECORD_TIMESTAMP, nuevoRecord.timestampMillis)
        }
    }

    /**
     * Obtiene el record actual; devuelve null si no existe (round <= 0).
     *
     * - Documentación SharedPreferences.getInt: https://developer.android.com/reference/android/content/SharedPreferences
     */
    fun obtenerRecord(context: Context): Record? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val round = prefs.getInt(KEY_RECORD_ROUND, 0)
        if (round <= 0) return null
        val ts = prefs.getLong(KEY_RECORD_TIMESTAMP, 0L)
        return Record(timestampMillis = ts, maxRound = round)
    }

    /**
     * Método auxiliar para borrar el record (útil en tests o debug).
     */
    fun borrarRecord(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }
}
