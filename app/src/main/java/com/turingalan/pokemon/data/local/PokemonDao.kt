package com.turingalan.pokemon.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * [CAPA DE DATOS - LOCAL - DAO]
 * Data Access Object (Objeto de Acceso a Datos).
 *
 * @Dao: Le dice a Room que esta interfaz contiene las funciones para acceder a la base de datos.
 * Room generará el código real (la implementación) automáticamente.
 */
@Dao
interface PokemonDao {

    /**
     * Inserta una fila en la tabla.
     * @suspend: IMPORTANTE. Las operaciones de base de datos son "bloqueantes" (tardan tiempo).
     * La palabra clave 'suspend' obliga a ejecutar esto dentro de una Corrutina (hilo secundario),
     * evitando que la app se congele (ANR).
     *
     * Retorna Long: El ID de la fila recién insertada.
     */
    @Insert
    suspend fun insert(pokemon: PokemonEntity): Long

    /**
     * Borra un elemento específico.
     * Retorna Int: El número de filas eliminadas (debería ser 1).
     */
    @Delete
    suspend fun deleteOne(pokemon: PokemonEntity): Int

    /**
     * Obtiene todos los Pokémons una sola vez.
     * Es una "foto" de la base de datos en este instante.
     */
    @Query("SELECT * FROM pokemon")
    suspend fun getAll(): List<PokemonEntity>

    /**
     * [FLOW - Programación Reactiva]
     * A diferencia de 'getAll()', esto devuelve un 'Flow'.
     *
     * ¿Qué es un Flow?
     * Es una tubería de datos viva.
     * 1. Cuando llamas a esta función, obtienes los datos actuales.
     * 2. Si MAÑANA alguien inserta un nuevo Pokémon en la BD, Room AUTOMÁTICAMENTE
     *    envía la nueva lista por esta tubería.
     * 3. La UI, que está escuchando ("collecting") este Flow, se actualiza sola.
     *
     * Nota: Los Flows de Room no necesitan 'suspend' porque son asíncronos por naturaleza.
     */
    @Query("SELECT * FROM pokemon")
    fun observeAll(): Flow<List<PokemonEntity>>

    /**
     * Busca un Pokémon por su ID.
     * Retorna PokemonEntity? (Nullable) porque puede que no exista un Pokémon con ese ID.
     */
    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun readPokemonById(id: Long): PokemonEntity?
}