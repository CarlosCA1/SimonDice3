package com.example.simondice2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // Importa getValueimport androidx.compose.runtime.collectAsState // Importa collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.simondice2.data.GameRecord // Asegúrate de importar tu clase de datos renombrada

@Composable
fun IU(vm: MyViewModel) {

    // Recolecta los estados del ViewModel de la forma correcta
    val ronda by vm.ronda.collectAsState()
    val msg by vm.msg.collectAsState()
    val recordState by vm.recordState.collectAsState()
    val habilitado by vm.habilitado.collectAsState()
    val iluminado by vm.iluminado.collectAsState()
    val nombre by vm.nombre.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Ahora usa las variables recolectadas, sin .value
        Text("Ronda: $ronda", fontSize = 25.sp)
        Text(msg, fontSize = 22.sp)
        Text("Nombre: $nombre")

        // El let sigue funcionando igual
        recordState?.let { record ->
            // Si quieres la función formattedDateTime(), debes crearla
            // Por ejemplo, como una función de extensión en un fichero nuevo.
            Text("Record: ${record.maxRound}  -  ${record.nombreJugador}", fontSize = 14.sp)
        } ?: Text("Record: —", fontSize = 14.sp)

        Row {
            // Pasa los estados recolectados a los componentes hijos
            BotonColor(
                color = Colores.CLASE_VERDE,
                iluminado = iluminado,
                habilitado = habilitado,
                onClick = { vm.comprobarJugador(Colores.CLASE_VERDE.ordinal) }
            )
            BotonColor(
                color = Colores.CLASE_ROJO,
                iluminado = iluminado,
                habilitado = habilitado,
                onClick = { vm.comprobarJugador(Colores.CLASE_ROJO.ordinal) }
            )
        }

        Row {
            BotonColor(
                color = Colores.CLASE_AZUL,
                iluminado = iluminado,
                habilitado = habilitado,
                onClick = { vm.comprobarJugador(Colores.CLASE_AZUL.ordinal) }
            )
            BotonColor(
                color = Colores.CLASE_AMARILLO,
                iluminado = iluminado,
                habilitado = habilitado,
                onClick = { vm.comprobarJugador(Colores.CLASE_AMARILLO.ordinal) }
            )
        }

        Button(onClick = { vm.startGame() }) {
            Text("Empezar")
        }
    }
}

@Composable
fun BotonColor(
    color: Colores,
    iluminado: Int,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    // La lógica ahora depende de los parámetros recibidos
    val activo = iluminado == color.ordinal

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (activo) color.color.copy(alpha = 0.4f) else color.color
        ),
        enabled = habilitado,
        onClick = onClick,
        modifier = Modifier
            .size(160.dp, 90.dp)
            .padding(8.dp)
    ) {
        Text(color.txt)
    }
}
