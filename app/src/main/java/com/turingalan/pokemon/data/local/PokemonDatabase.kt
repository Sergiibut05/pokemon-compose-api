package com.turingalan.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * [CAPA DE DATOS - LOCAL - DATABASE]
 *
 * Esta clase ABSTRACTA representa tu base de datos física en el dispositivo.
 * Room la usará para generar todo el código de conexión y gestión de SQLite.
 *
 * @Database: Configuración principal.
 * - entities = [PokemonEntity::class]: Le dices a Room: "Esta base de datos tendrá una tabla basada en esta clase".
 *   Si tienes más tablas (ej. Entrenadores), las añades aquí: [PokemonEntity::class, TrainerEntity::class].
 * - version = 1: Importante para migraciones. Si mañana añades una columna a la tabla, debes subir esto a 2.
 */
@Database(entities = [PokemonEntity::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {

    /**
     * [ACCESO A LOS DAOs]
     *
     * Aquí defines las "puertas" de acceso a tus tablas.
     * No necesitas implementar esta función; Room generará el código por ti.
     *
     * Cuando quieras acceder a la tabla 'pokemon', llamarás a: database.getPokemonDao().insert(...)
     */
    abstract fun getPokemonDao(): PokemonDao
}