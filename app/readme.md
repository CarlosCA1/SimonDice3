# Simón Dice con Jetpack Compose, MVVM y Room

Este proyecto implementa el clásico juego **Simón Dice** utilizando tecnologías modernas de Android, incluyendo **Kotlin**, **Jetpack Compose** para la UI, el patrón de arquitectura **MVVM** y **Room** para la persistencia de datos.

---

## Arquitectura General

La aplicación sigue el patrón MVVM (Model-View-ViewModel):

- **View (UI)**: Compuesta por funciones Composable (`IU`, `BotonColor`, etc.) que observan el estado del ViewModel y le notifican las interacciones del usuario.
- **ViewModel (`MyViewModel`)**: Contiene toda la lógica de negocio del juego y gestiona el estado de la UI. No tiene referencias a la UI, lo que facilita las pruebas y el mantenimiento.
- **Model (Datos)**: Representado por la capa de persistencia con Room (`RecordRepository`, `RecordDao`, `RecordEntity`, `AppDatabase`).

---

### `MyViewModel`

Es el cerebro de la aplicación. Gestiona la lógica del juego y expone el estado a la UI mediante `StateFlow`, siguiendo las mejores prácticas de Android.

- **Estado (`StateFlow`)**:
    - `msg`: Mensaje informativo para el usuario.
    - `iluminado`: El color del botón que se está iluminando.
    - `habilitado`: Si los botones de colores son interactivos.
    - `ronda`: La ronda actual.
    - `recordState`: El récord de puntuación máxima, cargado desde la base de datos.

- **Lógica del juego**:
    - `startGame()`: Inicia una nueva partida.
    - `siguienteRonda()`: Añade un color a la secuencia y la reproduce.
    - `comprobarJugador()`: Valida la pulsación del jugador.
    - `gameOver()`: Finaliza la partida y gestiona la actualización del récord si se ha superado.

---

### Persistencia de Datos con Room

Para guardar la puntuación máxima de forma persistente, se ha implementado la librería **Room**. Esta capa está formada por los siguientes componentes:

#### 1. `RecordEntity`
Es una `data class` que define la estructura (el esquema) de la tabla `record` en la base de datos. Cada instancia de esta clase representa una fila en la tabla.

```kotlin
@Entity(tableName = "record")
data class RecordEntity(
    @PrimaryKey val id: Int = 1, // Clave primaria fija para tener siempre una única fila
    val timestampMillis: Long,
    val maxRound: Int
)
```

#### 2. `RecordDao` (Data Access Object)
Es una `interface` donde se definen las consultas a la base de datos. Room genera automáticamente la implementación.

- `getRecord()`: Obtiene el récord actual.
- `insertRecord(record)`: Inserta o reemplaza el récord existente.
- `deleteRecord()`: Borra el récord.

```kotlin
@Dao
interface RecordDao {
    @Query("SELECT * FROM record WHERE id = 1")
    suspend fun getRecord(): RecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: RecordEntity)
}
```

#### 3. `AppDatabase`
Es la clase principal de la base de datos que hereda de `RoomDatabase`. Define qué `entities` (tablas) contiene la base de datos y proporciona acceso a los `DAO`.

Se implementa como un **singleton** para asegurar que solo exista una instancia de la base de datos en toda la aplicación.

```kotlin
@Database(entities = [RecordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        fun getDatabase(context: Context): AppDatabase { /* ... */ }
    }
}
```

#### 4. `RecordRepository`
Actúa como una fachada (façade) que abstrae el origen de datos (`RecordDao`). El `ViewModel` solo interactúa con el `Repository`, sin saber los detalles de implementación de la base de datos. Esto facilita el testing y permite cambiar la implementación de la persistencia en el futuro sin afectar al resto de la app.

```kotlin
class RecordRepository(private val dao: RecordDao) {
    suspend fun getRecord(): RecordEntity? = dao.getRecord()
    suspend fun saveRecord(record: RecordEntity) = dao.insertRecord(record)
}
```

### Integración en `MyViewModel`

El `ViewModel` obtiene una instancia del `Repository` y lo utiliza para cargar y guardar el récord, siempre dentro de corrutinas en el dispatcher `Dispatchers.IO` para no bloquear el hilo principal.

```kotlin
// En MyViewModel
private val repository: RecordRepository =
    RecordRepository(AppDatabase.getDatabase(application).recordDao())

// ... dentro de una corrutina
repository.saveRecord(newRecord.toEntity())
```
