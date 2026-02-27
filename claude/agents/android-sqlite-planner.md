---
name: android-sqlite-planner
description: Plan para guardar records usando SQLite en la app Simon Dice
---

# Objetivo
Guardar las puntuaciones en SQLite y mantener solo el TOP 10.

# Cambios necesarios

1. Base de datos
   Crear tabla "scores" con:

id
puntos
fecha

2. Guardar puntuación
   En MyViewModel.gameOver()
   llamar:

repository.guardarPuntuacion(ronda)

3. Limitar a 10 records
   Ordenar por:

puntos DESC
fecha ASC

Eliminar el resto.

4. Mostrar record máximo
   Usar:

repository.obtenerRecord()

Mostrar en IU.kt

5. Mensaje Logcat
   Antes de guardar:

repository.esTopDiez()

Mostrar:

Log.d("Juego","Forma parte de los diez primeros")

# Resultado esperado

Se guardan puntuaciones  
Solo hay 10 records  
Se muestra record en pantalla  
Mensaje Logcat si entra en top 10  