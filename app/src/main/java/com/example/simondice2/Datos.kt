package com.example.simondice2

import androidx.compose.ui.graphics.Color

object Datos {

    var ronda = 0
    var secuencia = mutableListOf<Int>()
    var indiceJugador = 0
    var mensaje = "Pulsa Start"
    var botonesHabilitados = false
    var botonActivo = -1

    var record: java.lang.Record? = null

    enum class Colores(val color: Color, val txt: String) {
        CLASE_VERDE(Color.Green, "Verde"),
        CLASE_ROJO(Color.Red, "Rojo"),
        CLASE_AZUL(Color.Blue, "Azul"),
        CLASE_AMARILLO(Color.Yellow, "Amarillo")
    }
}
