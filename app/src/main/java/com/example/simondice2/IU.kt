package com.example.simondice2

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IU(miViewModel: MyViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically) // <-- CAMBIO AQUÍ
    ) {
        // PRIMERA FILA: Botón de puntuación
        Row {
            TextoScore()
        }

        // SEGUNDA FILA: Botones de colores (rojo, verde)
        Row {
            Boton(miViewModel, Colores.CLASE_ROJO)
            Boton(miViewModel, Colores.CLASE_VERDE)
        }

        // TERCERA FILA: Botones de colores (azul, amarillo)
        Row {
            Boton(miViewModel, Colores.CLASE_AZUL)
            Boton(miViewModel, Colores.CLASE_AMARILLO)
        }

        // CUARTA FILA: Botón de Start
        Row {
            BotonStart(miViewModel, Colores.CLASE_START)
        }
    }
}


@Composable
fun TextoScore() {
    // utilizamos el texto del enum
    Text(text = "Score: ", fontSize = 25.sp, color = Color.Black)
}


@Composable
fun Boton(miViewModel: MyViewModel, enum_color: Colores) {
    Button(
        // utilizamos el color del enum
        colors =  ButtonDefaults.buttonColors(enum_color.color),
        onClick = { Log.d("Juego","Click!")
            miViewModel.comprobar(enum_color.ordinal)
        },
        shape = RectangleShape,
        modifier = Modifier
            .size((180).dp, (100).dp)
            .padding(10.dp)
    ) {
        // utilizamos el texto del enum
        Text(text = enum_color.txt, fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun Boton2(miViewModel: MyViewModel, enum_color: Colores) {
    Button(
        // utilizamos el color del enum
        colors =  ButtonDefaults.buttonColors(enum_color.color),
        onClick = { Log.d("Juego","Click!")
            miViewModel.comprobar(enum_color.ordinal)
        },
        shape = RectangleShape,
        modifier = Modifier
            .size((180).dp, (100).dp)
            .padding(10.dp)
    ) {
        // utilizamos el texto del enum
        Text(text = enum_color.txt, fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun BotonStart(miViewModel: MyViewModel, enum_color: Colores){
    Button(
        // utilizamos el color del enum
        colors =  ButtonDefaults.buttonColors(enum_color.color),
        onClick = { Log.d("Juego", "Click!")
            miViewModel.crearRandom()
        },
        modifier = Modifier
            .size((130).dp, (70).dp)
            .padding(15.dp)
    ) {
        Text(text = "Start", fontSize = 20.sp)
    }
}

/**
 * Preview de la interfaz de usuario
 */

@Preview(showBackground = true)
@Composable
fun IUPreview() {
    IU(MyViewModel())
}