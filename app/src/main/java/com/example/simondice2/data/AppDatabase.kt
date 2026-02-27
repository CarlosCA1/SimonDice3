
package com.example.simondice2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase principal de la base de datos de la aplicación, implementada con Room.
 *
 * Esta clase abstracta sirve como el punto de acceso central a los datos persistentes.
 *
 * @see [RoomDatabase](https://developer.android.com/reference/androidx/room/RoomDatabase)
 */
@Database(
    entities = [RecordEntity::class], // Lista de todas las entidades (tablas) que pertenecen a esta base de datos.
    version = 3,                      // Versión de la base de datos. Debe incrementarse al cambiar el esquema.
    exportSchema = false              // Desactiva la exportación del esquema. Para un proyecto real, se recomienda mantenerlo en `true`.
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al Data Access Object (DAO) para la entidad `RecordEntity`.
     *
     * Room implementará automáticamente este método abstracto para devolver una instancia de `RecordDao`.
     *
     * @return Una instancia de [RecordDao].
     */
    abstract fun recordDao(): RecordDao

    /**
     * Objeto `companion` para implementar el patrón Singleton, asegurando que solo exista
     * una instancia de la base de datos en toda la aplicación para evitar problemas de concurrencia
     * y consumo de recursos.
     */
    companion object {
        /**
         * La instancia única de `AppDatabase`.
         * La anotación `@Volatile` asegura que los cambios en esta variable sean visibles
         * inmediatamente para todos los hilos, previniendo problemas de caché en entornos multihilo.
         */
        @Volatile private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia Singleton de la base de datos.
         *
         * Si la instancia ya existe, la devuelve. Si es nula, la crea de forma segura
         * dentro de un bloque `synchronized` para evitar que múltiples hilos creen
         * varias instancias al mismo tiempo (race condition).
         *
         * @param context El contexto de la aplicación.
         * @return La instancia Singleton de [AppDatabase].
         */
        fun getDatabase(context: Context): AppDatabase =
            // Si la instancia no es nula, la devuelve.
            INSTANCE ?: synchronized(this) {
                // Dentro del bloque sincronizado, vuelve a comprobar si la instancia fue creada
                // por otro hilo mientras este esperaba el bloqueo.
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, // Usa el contexto de la aplicación para evitar fugas de memoria.
                    AppDatabase::class.java,    // La clase de la base de datos.
                    "simondice_db"              // El nombre del fichero de la base de datos en el dispositivo.
                )
                    .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it } // Asigna la nueva instancia y la devuelve.
            }
    }
}
