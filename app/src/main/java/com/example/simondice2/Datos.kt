package com.example.simondice2

import androidx.compose.ui.graphics.Color
import com.example.simondice2.data.Record

object Datos {
    var ronda = 0
    var secuencia = mutableListOf<Int>()
    var indiceJugador = 0
    var mensaje = "Pulsa Start"
    var botonesHabilitados = false
    var botonActivo = -1

    // Record en memoria (se sincroniza desde ViewModel con SharedPreferences)
    var record: Record? = null

    /** Estados del juego */
    enum class Estado {
        INICIO, SECUENCIA, ESPERANDO, ENTRADA, COMPROBANDO, FINALIZADO
    }

    /** Colores */
    enum class Colores(val color: Color, val txt: String) {
        CLASE_VERDE(Color.Green, "verde"),      // 0
        CLASE_ROJO(Color.Red, "vermello"),      // 1
        CLASE_AZUL(Color.Blue, "azul"),         // 2
        CLASE_AMARILLO(Color.Yellow, "amarelo") // 3
    }
}
