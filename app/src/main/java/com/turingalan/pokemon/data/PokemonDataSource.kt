package com.turingalan.pokemon.data

import com.turingalan.pokemon.data.model.Pokemon
import kotlinx.coroutines.flow.Flow

/**
 * [CAPA DE DATOS - CONTRATO (INTERFACE)]
 *
 * Esta interfaz define QUÉ operaciones de datos soporta nuestra aplicación,
 * sin importar CÓMO se implementen (si es base de datos, red, memoria, archivo...).
 *
 * Esto permite que 'PokemonLocalDataSource' y 'PokemonRemoteDataSource' sean intercambiables
 * y que el Repositorio pueda hablar con ambos usando el mismo "idioma".
 */
interface PokemonDataSource {

    /**
     * Guarda una lista de Pokémons.
     * - En Local: Hace un INSERT en la base de datos.
     * - En Remoto: Podría hacer un POST a la API (aunque en este ejemplo no se usa).
     */
    suspend fun addAll(pokemonList: List<Pokemon>)

    /**
     * Observa los cambios en los datos (Reactividad).
     * - En Local: Devuelve un Flow que emite cada vez que la tabla cambia.
     * - En Remoto: Podría ser un socket o polling (raro en REST APIs simples).
     */
    fun observe(): Flow<Result<List<Pokemon>>>

    /**
     * Lee todos los datos una vez (Snapshot).
     */
    suspend fun readAll(): Result<List<Pokemon>>

    /**
     * Lee un solo dato por ID.
     */
    suspend fun readOne(id: Long): Result<Pokemon>

    /**
     * Función auxiliar para manejo de errores (parece experimental en tu código).-
     */
    suspend fun isError()
}