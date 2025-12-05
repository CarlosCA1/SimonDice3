# Implementación del Récord en Simon Dice

Este documento detalla cómo se ha implementado el sistema de puntuación máxima (récord) en la aplicación "Simon Dice".

## Arquitectura

La funcionalidad de récord se divide en tres componentes principales:

1.  **`Record.kt`**: Una clase de datos (`data class`) que modela la información del récord.
2.  **`ControladorPreference.kt`**: Un objeto singleton que gestiona la persistencia del récord en `SharedPreferences`.
3.  **`MyViewModel.kt`**: El ViewModel que integra la lógica de juego con el sistema de persistencia y expone el estado del récord a la UI.

---

### 1. `Record.kt` (Modelo de Datos)

Es una `data class` simple que contiene la información esencial de un récord:

- `timestampMillis` (Long): El momento exacto en que se consiguió el récord, medido en milisegundos desde la Época (Epoch).
- `maxRound` (Int): La ronda más alta alcanzada.

También incluye un método `formattedDateTime()` para convertir el timestamp en una cadena de texto legible (`yyyy-MM-dd HH:mm:ss`).

```kotlin
// app/src/main/java/com/example/simondice2/data/Record.kt

data class Record(
    val timestampMillis: Long,
    val maxRound: Int
) {
    fun formattedDateTime(zone: ZoneId = ZoneId.systemDefault()): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zone)
        return formatter.format(Instant.ofEpochMilli(timestampMillis))
    }
}
```

---

### 2. `ControladorPreference.kt` (Capa de Persistencia)

Este objeto se encarga de guardar y leer el récord del dispositivo. Para simplificar, se utiliza `SharedPreferences`.

**Características principales**:

- **Singleton**: Se utiliza el patrón `object` de Kotlin para asegurar una única instancia.
- **Claves**: Define claves (`KEY_RECORD_ROUND`, `KEY_RECORD_TIMESTAMP`) para almacenar los datos.
- **Operaciones CRUD**: 
    - `actualizarRecord(context, nuevoRecord)`: Guarda o sobrescribe el récord actual. Utiliza `edit{}` de Android KTX para una operación más concisa.
    - `obtenerRecord(context)`: Recupera el récord. Devuelve `null` si no hay ninguno guardado.
    - `borrarRecord(context)`: Elimina el récord, útil para depuración.

Las operaciones de `SharedPreferences` son síncronas, por lo que se invocarán desde un hilo de fondo en el `ViewModel` para no bloquear la UI.

```kotlin
// app/src/main/java/com/example/simondice2/ControladorPreference.kt

object ControladorPreference {
    private const val PREFS_NAME = "simondice_record_prefs"
    private const val KEY_RECORD_ROUND = "record_round"
    private const val KEY_RECORD_TIMESTAMP = "record_timestamp"

    fun actualizarRecord(context: Context, nuevoRecord: Record) { /* ... */ }

    fun obtenerRecord(context: Context): Record? { /* ... */ }
}
```

---

### 3. `MyViewModel.kt` (Lógica y Estado)

El `ViewModel` actúa como intermediario entre la lógica del juego y la capa de persistencia.

**Responsabilidades**:

1.  **Carga Inicial**: En su bloque `init`, lanza una corrutina en `Dispatchers.IO` para cargar el récord desde `ControladorPreference` sin afectar al hilo principal. El resultado se almacena en `recordState`.

2.  **Estado del Récord**: Mantiene el récord en una variable de estado de Compose, `recordState`, para que la UI pueda reaccionar a sus cambios.

    ```kotlin
    var recordState = mutableStateOf<Record?>(Datos.record)
    ```

3.  **Comprobación en `gameOver()`**: Cuando el jugador pierde, el método `gameOver()` se ejecuta. Dentro de este método:
    - Se lanza una corrutina en `Dispatchers.IO`.
    - Se obtiene el récord actual.
    - Se compara la ronda alcanzada en la partida (`rondaLlegada`) con la ronda máxima del récord (`mejorActual`).
    - Si se ha superado, se crea una nueva instancia de `Record` con el `timestamp` actual y la nueva puntuación.
    - Se llama a `ControladorPreference.actualizarRecord()` para persistir el nuevo récord.
    - Finalmente, se actualiza `recordState.value` para que la UI refleje el nuevo récord de inmediato.

4.  **Reseteo**: Incluye una función `resetRecord()` para facilitar la eliminación del récord durante el desarrollo y las pruebas.

```kotlin
// app/src/main/java/com/example/simondice2/MyViewModel.kt

class MyViewModel(application: Application) : AndroidViewModel(application) {
    
    var recordState = mutableStateOf<Record?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recordState.value = ControladorPreference.obtenerRecord(getApplication())
        }
    }

    private fun gameOver() {
        // ...
        viewModelScope.launch(Dispatchers.IO) {
            val actual = ControladorPreference.obtenerRecord(getApplication())
            val mejorActual = actual?.maxRound ?: 0
            if (rondaLlegada > mejorActual) {
                val newRecord = Record(/* ... */)
                ControladorPreference.actualizarRecord(getApplication(), newRecord)
                recordState.value = newRecord
            }
        }
    }
}
```

## Integración con la UI (Jetpack Compose)

La interfaz de usuario (por ejemplo, en `MainActivity.kt`) observaría el estado `recordState` del `ViewModel`. Cuando `recordState.value` cambie, la parte de la UI que muestra el récord (por ejemplo, un `Text`) se recompondrá automáticamente para mostrar la nueva puntuución máxima y la fecha.

```kotlin
// En la UI de Compose
val record = myViewModel.recordState.value

if (record != null) {
    Text(text = "Récord: ${record.maxRound} (${record.formattedDateTime()})")
} else {
    Text(text = "Récord: Aún no hay récord")
}
```
