package com.example.simondice2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.simondice2.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val nombreJugador = "Carlos"

    // 1. Estado interno y privado del ViewModel
    private val _msg = MutableStateFlow("Toca 'Empezar' para jugar")
    private val _iluminado = MutableStateFlow(-1) // -1 significa ningún botón iluminado
    private val _habilitado = MutableStateFlow(false)
    private val _ronda = MutableStateFlow(0)
    private val _recordState = MutableStateFlow<GameRecord?>(null) // Usamos el nuevo nombre

    private val _nombre = MutableStateFlow(nombreJugador)

    private val secuencia = mutableListOf<Int>()
    private var indiceJugador = 0

    // 2. Estado público e inmutable para la UI
    val msg: StateFlow<String> = _msg.asStateFlow()
    val iluminado: StateFlow<Int> = _iluminado.asStateFlow()
    val habilitado: StateFlow<Boolean> = _habilitado.asStateFlow()
    val ronda: StateFlow<Int> = _ronda.asStateFlow()
    val recordState: StateFlow<GameRecord?> = _recordState.asStateFlow()

    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val repository: RecordRepository =
        RecordRepository(AppDatabase.getDatabase(application).recordDao())

    init {
        // Carga el récord inicial desde la base de datos
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRecord()?.let { entity ->
                _recordState.value = entity.toDomain() // toDomain() ahora devuelve un GameRecord
            }
        }
    }

    fun startGame() {
        secuencia.clear()
        _ronda.value = 0
        indiceJugador = 0
        siguienteRonda()
    }

    private fun siguienteRonda() {
        _ronda.value++
        indiceJugador = 0
        _habilitado.value = false
        _msg.value = "Simón muestra"

        secuencia.add((0..3).random())
        reproducirSecuencia()
    }

    private fun reproducirSecuencia() {
        viewModelScope.launch {
            for (color in secuencia) {
                _iluminado.value = color
                delay(500)
                _iluminado.value = -1 // Apaga el botón
                delay(250)
            }
            _msg.value = "Tu turno"
            _habilitado.value = true
        }
    }

    fun comprobarJugador(color: Int) {
        if (!_habilitado.value) return // Comprueba el estado del ViewModel

        // Ilumina el botón que pulsa el jugador
        viewModelScope.launch {
            _iluminado.value = color
            delay(300)
            _iluminado.value = -1
        }

        if (color == secuencia[indiceJugador]) {
            indiceJugador++
            // Si el jugador ha completado la secuencia de la ronda
            if (indiceJugador == secuencia.size) {
                _habilitado.value = false
                _msg.value = "¡Bien!"
                viewModelScope.launch {
                    delay(800)
                    siguienteRonda()
                }
            }
        } else {
            gameOver()
        }
    }

    private fun gameOver() {
        _habilitado.value = false
        _msg.value = "¡Has perdido! Nivel: ${_ronda.value}"
        val rondaLlegada = _ronda.value

        viewModelScope.launch(Dispatchers.IO) {
            val mejorActual = _recordState.value?.maxRound ?: 0
            if (rondaLlegada > mejorActual) {
                val newRecord = GameRecord( // Usamos el nuevo nombre
                    timestampMillis = Instant.now().toEpochMilli(),
                    maxRound = rondaLlegada,
                    nombreJugador = nombreJugador
                )
                repository.saveRecord(newRecord.toEntity()) // toEntity() debe aceptar un GameRecord
                _recordState.value = newRecord
            }
        }
    }

    fun resetRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecord()
            _recordState.value = null
        }
    }
}
