---
name: android-room-planner
description: Plan para guardar record con nombre usando Room
---

# Objetivo
Guardar el record junto con el nombre del jugador usando Room.

# Cambios necesarios

1. Entity
   Añadir campo:

nombreJugador

2. ViewModel
   Crear variable:

private val nombreJugador = "Carlos"

3. Guardar record
   Cuando termina la partida:

guardar:

maxRound
timestamp
nombreJugador

4. Mostrar en UI

Mostrar:

Record de Carlos: 7

# Resultado esperado

Se guarda el record con nombre  
El nombre aparece en pantalla  
El nombre se puede cambiar desde el ViewModel  