# Juego Simón Dice en Android (Jetpack Compose)

Este proyecto implementa el clásico juego **Simón Dice** utilizando **Kotlin**, **Jetpack Compose** y el patrón **MVVM**.  
El objetivo es que el jugador repita la secuencia de colores que muestra Simón, aumentando la dificultad en cada ronda.

---

## Estructura del proyecto

### `MainActivity`
- Punto de entrada de la aplicación.
- Inicializa el **ViewModel** (`MyViewModel`).
- Configura la interfaz de usuario con `setContent { IU(miViewModel) }`.
- Activa `enableEdgeToEdge()` para aprovechar toda la pantalla.

---

### `MyViewModel`
Clase que gestiona la **lógica del juego** y el estado:

- **Estados observables**:
    - `msg`: mensaje actual (ej. "Tu turno", "Bien!", "Has Perdido").
    - `iluminado`: botón que se está iluminando.
    - `habilitado`: controla si los botones están activos para el jugador.
    - `ronda`: número de ronda actual.

- **Funciones principales**:
    - `startGame()`: reinicia el juego y comienza la primera ronda.
    - `siguienteRonda()`: añade un nuevo color a la secuencia y prepara la ronda.
    - `reproducirSecuencia()`: Simón muestra la secuencia iluminando los botones con retardos (`delay`).
    - `comprobarJugador(color: Int)`: valida la entrada del jugador contra la secuencia.
    - `gameOver()`: finaliza el juego mostrando el nivel alcanzado.

---

### `IU`
Composable que define la **interfaz gráfica**:

- Muestra:
    - El **score** (ronda actual).
    - El **mensaje** del juego.
- Contiene:
    - Cuatro botones de colores (`BotonColor`).
    - Un botón de inicio (`BotonStart`).

---

### `BotonColor`
- Representa cada botón de color (rojo, verde, azul, amarillo).
- Cambia de apariencia si está iluminado (`alpha` reducido).
- Al pulsar:
    - Llama a `comprobarJugador()`.
    - Da feedback visual iluminando el botón brevemente.

---

### `BotonStart`
- Reinicia el juego llamando a `startGame()`.
- Botón más pequeño que los de colores.

---

### `Datos`
Objeto que almacena el **estado global del juego**:
- `ronda`: número de ronda actual.
- `secuencia`: lista de colores que Simón debe mostrar.
- `indiceJugador`: posición actual del jugador en la secuencia.
- `mensaje`: texto mostrado en pantalla.
- `botonesHabilitados`: indica si el jugador puede interactuar.
- `botonActivo`: botón que se ilumina en cada momento.

---

### `Colores`
Enum que define los colores del juego:
- Verde (`CLASE_VERDE`)
- Rojo (`CLASE_ROJO`)
- Azul (`CLASE_AZUL`)
- Amarillo (`CLASE_AMARILLO`)

Cada color tiene:
- Un valor `Color` de Compose.
- Un texto descriptivo.

---

## Flujo del juego
1. El jugador pulsa **Start**.
2. Simón genera una secuencia y la muestra iluminando botones.
3. El jugador debe repetir la secuencia correctamente.
4. Si acierta toda la secuencia:
    - Avanza a la siguiente ronda.
    - La secuencia se alarga.
5. Si falla:
    - Se muestra **Game Over** con el nivel alcanzado.