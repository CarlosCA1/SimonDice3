package com.example.simondice2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.simondice2.data.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import java.time.Instant

/**
 * ViewModel que hereda de [AndroidViewModel] para poder acceder al [Application] context.
 *
 * Se encarga de la lógica de negocio y de gestionar el estado de la UI.
 */
class MyViewModel(application: Application) : AndroidViewModel(application) {

    /** Mensaje informativo para el usuario (ej: "Tu turno"). Gestionado con [mutableStateOf].
     */
    var msg = mutableStateOf(Datos.mensaje)

    /** Color del botón que se ilumina. -1 si ninguno. Gestionado con [mutableStateOf]. */
    var iluminado = mutableStateOf(Datos.botonActivo)

    /** Controla si los botones de colores son pulsables por el jugador. Gestionado con [mutableStateOf]. */
    var habilitado = mutableStateOf(Datos.botonesHabilitados)

    /** Ronda actual del juego. Gestionado con [mutableStateOf]. */
    var ronda = mutableStateOf(Datos.ronda)

    /**
     * El récord actual (puntuación máxima). Es `null` si no hay récord.
     * Gestionado con [mutableStateOf] para que la UI de Compose reaccione a sus cambios.
     * @see [mutableStateOf](https://developer.android.com/jetpack/compose/state#managing-state)
     */
    var recordState = mutableStateOf<Record?>(Datos.record)

    init {
        // Cargamos el record persistente al inicializar el ViewModel.
        // Se usa viewModelScope para lanzar una corrutina que sobrevive a cambios de configuración.
        // Se ejecuta en el dispatcher IO para no bloquear el hilo principal.
        // @see [viewModelScope](https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope)
        // @see [Dispatchers.IO](https://developer.android.com/kotlin/coroutines/coroutines-adv#dispatchers)
        viewModelScope.launch(Dispatchers.IO) {
            val r = ControladorPreference.obtenerRecord(getApplication())
            r?.let {
                recordState.value = it
                Datos.record = it
            }
        }
    }

    /** Inicia una nueva partida, reseteando los contadores y la secuencia. */
    fun startGame() {
        Datos.secuencia.clear()
        Datos.ronda = 0
        Datos.indiceJugador = 0
        siguienteRonda()
    }

    /** Avanza a la siguiente ronda, añadiendo un nuevo color a la secuencia de Simón. */
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

    /**
     * Reproduce la secuencia de colores actual. Lanza una corrutina en [viewModelScope].
     */
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

    /**
     * Comprueba si el botón pulsado por el jugador es correcto.
     * @param color El color (0-3) que ha pulsado el jugador.
     */
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

    /**
     * Finaliza la partida. Comprueba si se ha superado el récord y lo guarda si es necesario.
     * La comprobación y guardado se hacen en una corrutina con [Dispatchers.IO].
     */
    private fun gameOver() {
        Datos.botonesHabilitados = false
        habilitado.value = false

        Datos.mensaje = "¡Has Perdido! Nivel: ${Datos.ronda}"
        msg.value = Datos.mensaje

        // Comprobar y guardar record si procede
        // Guardamos la ronda que acaba de completarse en una variable
        val rondaLlegada = Datos.ronda


        viewModelScope.launch(Dispatchers.IO) {

            // Obtenemos el récord actual desde las preferencias compartidas
            val actual = ControladorPreference.obtenerRecord(getApplication())

            // Si existe un récord, usamos su valor de maxRound; si no, asumimos 0
            val mejorActual = actual?.maxRound ?: 0

            // Comparamos la ronda recién llegada con el récord actual
            if (rondaLlegada > mejorActual) {

                // Creamos un nuevo récord con la fecha actual y la nueva ronda máxima alcanzada
                val newRecord = Record(
                    timestampMillis = Instant.now().toEpochMilli(), // tiempo en milisegundos
                    maxRound = rondaLlegada // nueva ronda máxima
                )

                // Actualizamos el récord en las preferencias compartidas
                ControladorPreference.actualizarRecord(getApplication(), newRecord)

                // Actualizamos los estados en el hilo principal
                // recordState probablemente sea un MutableStateFlow o LiveData
                recordState.value = newRecord

                // Actualizamos también el récord global de Datos
                Datos.record = newRecord
            }
        }
    }

    /**
     * Método útil para resetear el record (debug/tests). Lanza la operación en [Dispatchers.IO].
     * @see [Dispatchers.IO](https://developer.android.com/kotlin/coroutines/coroutines-adv#dispatchers)
     */
    fun resetRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            ControladorPreference.borrarRecord(getApplication())
            recordState.value = null
            Datos.record = null
        }
    }
}
