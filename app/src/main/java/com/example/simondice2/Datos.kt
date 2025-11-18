package com.example.simondice2

import androidx.compose.ui.graphics.Color

/**
 * Clase para almacenar los datos del juego
 * @property ronda Número de ronda actual
 * @property secuencia Secuencia de colores
 * @property secuenciaUsuario Secuencia de colores introducida por el usuario
 * @property record Record de puntuación
 * @property estado Estado del juego
 */
object Datos {
    var ronda = 0
    var secuencia = mutableListOf<Int>()
    var secuenciaUsuario = mutableListOf<Int>()
    var record = 0
    var estado = Estado.INICIO
    var numero = 0
}

/**
 * Enum con los estados del juego
 *
 */

enum class Estado {
    INICIO,
    SECUENCIA,
    ESPERANDO,
    ENTRADA,
    COMPROBANDO,
    FINALIZADO
}

/**
 * Colores utilizados
 */

enum class Colores(val color: Color, val txt: String) {
    CLASE_ROJO(color = Color.Red, txt = "vermello"),
    CLASE_VERDE(color = Color.Green, txt = "verde"),
    CLASE_AZUL(color = Color.Blue, txt = "azul"),
    CLASE_AMARILLO(color = Color.Yellow, txt = "amarelo"),
    CLASE_START(color = Color.LightGray, txt = "Start")
}