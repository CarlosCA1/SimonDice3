# MongoDB
Objetivo: implementar un sistema avanzado de persistencia de datos que combina almacenamiento local (rápido y robusto) con almacenamiento en la nube (sincronización global).

## 🚀 Hoja de Ruta e Implementación de Tareas

A continuación se detallan los hitos clave en el desarrollo del sistema de récords y persistencia:

### 🔹 TASK-001: Configuración de MongoDB Atlas
**Descripción:** Preparación del entorno para la conectividad con la nube.
- **Objetivos:** 
  - Integración del SDK de MongoDB (Kotlin Coroutine Driver).
  - Configuración del permiso `INTERNET` en el manifiesto.
  - Implementación de la clase Singleton `MongoConfig` para gestionar la conexión segura con el clúster de Atlas.
- **Detalles técnicos:** Uso de `org.mongodb:mongodb-driver-kotlin-coroutine` y gestión de URI sensible mediante variables de configuración.

### 🔹 TASK-002: Modelo de Datos y Servicio Cloud
**Descripción:** Definición de la estructura de documentos y lógica CRUD en la nube.
- **Objetivos:** 
  - Creación del modelo `MongoRecord` compatible con BSON.
  - Implementación de `MongoService` para inserción y recuperación de récords globales.
- **Detalles técnicos:** Mapeo de `GameRecord` a `Document`, uso de `coroutineScope` para operaciones asíncronas no bloqueantes y definición de la base de datos `simon_dice_db`.

### 🔹 TASK-003: Orquestación de Persistencia Triple
**Descripción:** Gestión jerárquica de datos en tres niveles: SharedPreferences, Room y MongoDB.
- **Objetivos:** 
  - Actualización de `RecordRepository` para orquestar los tres sistemas.
  - Implementación de **"Guardado en Cascada"**:
    1. **SharedPreferences:** Acceso inmediato y ligero.
    2. **SQLite (Room):** Persistencia local estructurada y robusta.
    3. **MongoDB Atlas:** Sincronización en la nube (si hay conexión).
- **Detalles técnicos:** Inyección de dependencias, manejo de excepciones de red con bloques `try-catch` específicos para evitar que fallos en la nube afecten al guardado local.

### 🔹 TASK-004: Feedback de UI y Sincronización
**Descripción:** Información en tiempo real al usuario sobre el estado de sus datos.
- **Objetivos:** 
  - Indicadores visuales de sincronización (icono/texto).
  - Botón de "Sincronización forzada" para actualizar datos manualmente.
  - Gestión de estados de carga (`isSyncing`) en la UI de Compose.
- **Detalles técnicos:** Uso de `StateFlow` para el estado de sincronización y `LaunchedEffect` para disparar comprobaciones automáticas al inicio.

---

## 🛠️ Tecnologías Utilizadas
- **UI:** Jetpack Compose
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Base de Datos Local:** Room (SQLite)
- **Persistencia Rápida:** SharedPreferences
- **Base de Datos Cloud:** MongoDB Atlas
- **Asincronía:** Kotlin Coroutines & Flow
