package com.example.simondice2

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MyViewModel(): ViewModel() {

    // etiqueta para logcat
    private val TAG_LOG = "miDebug"

    // este va a ser nuestra lista para la secuencia random
    // usamos mutable, ya que la queremos modificar
    var _numbers = mutableStateOf(0)

    // inicializamos variables cuando instanciamos
    init {
        Log.d(TAG_LOG, "Inicializamos ViewModel")
    }

    /**
     * crear entero random
     */
    fun crearRandom() {
        _numbers.value = (0..3).random()
        Log.d(TAG_LOG, "creamos random ${_numbers.value}")
        actualizarNumero(_numbers.value)
    }

    fun actualizarNumero(numero: Int) {
        Log.d(TAG_LOG, "actualizamos numero en Datos")
        Datos.numero = numero
    }

    /**
     * comprobar si el boton pulsado es el correcto
     * @param ordinal: Int numero de boton pulsado
     * @return Boolean si coincide TRUE, si no FALSE
     */
    fun comprobar(ordinal: Int): Boolean {
        Log.d(TAG_LOG, "comprobamos")
        return if (ordinal == Datos.numero) {
            Log.d(TAG_LOG, "es correcto")
            true
        } else {
            Log.d(TAG_LOG, "no es correcto")
            false
        }
    }
}