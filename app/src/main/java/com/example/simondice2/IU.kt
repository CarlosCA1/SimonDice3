package com.example.simondice2

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IU(miViewModel: MyViewModel) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Score: ${miViewModel.ronda.value}", fontSize = 25.sp)

        Text(miViewModel.msg.value, fontSize = 22.sp)

        Row {
            BotonColor(miViewModel, Colores.CLASE_ROJO)
            BotonColor(miViewModel, Colores.CLASE_VERDE)
        }

        Row {
            BotonColor(miViewModel, Colores.CLASE_AZUL)
            BotonColor(miViewModel, Colores.CLASE_AMARILLO)
        }

        Row {
            BotonStart(miViewModel)
        }
    }
}


@Composable
fun BotonColor(miViewModel: MyViewModel, enum_color: Colores) {

    val isActive = miViewModel.iluminado.value == enum_color.ordinal

    Button(
        colors = ButtonDefaults.buttonColors(
            if (isActive) enum_color.color.copy(alpha = 0.40f)
            else enum_color.color
        ),
        enabled = miViewModel.habilitado.value,
        onClick = {
            Log.d("Juego", "Click!")
            miViewModel.comprobarJugador(enum_color.ordinal)
        },
        modifier = Modifier
            .size(180.dp, 100.dp)
            .padding(10.dp)
    ) {
        Text(enum_color.txt, fontSize = 20.sp)
    }
}


@Composable
fun BotonStart(miViewModel: MyViewModel) {

    Button(
        onClick = { miViewModel.startGame() },
        modifier = Modifier
            .size(130.dp, 70.dp)
            .padding(15.dp)
    ) {
        Text("Start", fontSize = 20.sp)
    }
}
