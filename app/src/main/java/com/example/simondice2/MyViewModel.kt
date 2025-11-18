package com.example.simondice2

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    var msg = mutableStateOf(Datos.mensaje)
    var iluminado = mutableStateOf(Datos.botonActivo)
    var habilitado = mutableStateOf(Datos.botonesHabilitados)
    var ronda = mutableStateOf(Datos.ronda)

    /** Iniciar juego */
    fun startGame() {
        Datos.secuencia.clear()
        Datos.ronda = 0
        Datos.indiceJugador = 0
        siguienteRonda()
    }

    /** Nueva ronda */
    private fun siguienteRonda() {

        Datos.ronda++
        ronda.value = Datos.ronda

        Datos.indiceJugador = 0
        Datos.botonesHabilitados = false
        habilitado.value = false

        Datos.mensaje = "Simón muestra"
        msg.value = Datos.mensaje

        Datos.secuencia.add((0..3).random())

        reproducirSecuencia()
    }

    /** Simón reproduce la secuencia */
    private fun reproducirSecuencia() {

        viewModelScope.launch {

            for (n in Datos.secuencia) {

                Datos.botonActivo = n
                iluminado.value = Datos.botonActivo
                delay(500)

                Datos.botonActivo = -1
                iluminado.value = Datos.botonActivo
                delay(250)
            }

            Datos.mensaje = "Tu turno"
            msg.value = Datos.mensaje

            Datos.botonesHabilitados = true
            habilitado.value = true
        }
    }

    /** El jugador pulsa un botón */
    fun comprobarJugador(color: Int) {

        if (!Datos.botonesHabilitados) return

        // iluminar para feedback
        Datos.botonActivo = color
        iluminado.value = Datos.botonActivo

        viewModelScope.launch {
            delay(300)
            Datos.botonActivo = -1
            iluminado.value = -1
        }

        // comprobar si coincide
        if (color == Datos.secuencia[Datos.indiceJugador]) {

            Datos.indiceJugador++

            if (Datos.indiceJugador == Datos.secuencia.size) {
                Datos.botonesHabilitados = false
                habilitado.value = false

                Datos.mensaje = "Bien!"
                msg.value = Datos.mensaje

                viewModelScope.launch {
                    delay(800)
                    siguienteRonda()
                }
            }

        } else {
            gameOver()
        }
    }

    /** Game Over */
    private fun gameOver() {
        Datos.botonesHabilitados = false
        habilitado.value = false

        Datos.mensaje = "¡Has Perdido! Nivel: ${Datos.ronda}"
        msg.value = Datos.mensaje
    }
}