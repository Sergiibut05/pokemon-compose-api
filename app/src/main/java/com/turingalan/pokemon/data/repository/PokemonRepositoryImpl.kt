package com.turingalan.pokemon.data.repository

import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.di.LocalDataSource
import com.turingalan.pokemon.di.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [REPOSITORIO - IMPLEMENTACIÓN]
 *
 * El Repositorio es la PIEZA CLAVE de Clean Architecture.
 * Es el "Director de Orquesta" de los datos.
 *
 * Responsabilidades:
 * 1. Decidir de dónde sacar los datos (¿Base de datos? ¿Internet? ¿Memoria?).
 * 2. Coordinar la sincronización (Descargar de internet -> Guardar en BD -> Mostrar BD).
 * 3. Abstraer a la UI de la complejidad de los datos. El ViewModel no sabe si hay base de datos o no.
 */
class PokemonRepositoryImpl @Inject constructor(
    // Inyectamos las dos fuentes de datos con los calificadores @Local y @Remote para distinguirlas
    @RemoteDataSource private val remoteDataSource: PokemonDataSource,
    @LocalDataSource private val localDataSource: PokemonDataSource,
    private val scope: CoroutineScope
): PokemonRepository {

    /**
     * Lee un detalle. En este ejemplo simple, va directo a la red.
     * En una app más robusta, primero miraría en local.
     */
    override suspend fun readOne(id: Long): Result<Pokemon> {
        return remoteDataSource.readOne(id)
    }

    /**
     * Lee todos. Va directo a la red.
     */
    override suspend fun readAll(): Result<List<Pokemon>> {
        return remoteDataSource.readAll()
    }

    /**
     * [OBSERVE - SINGLE SOURCE OF TRUTH (SSOT)]
     *
     * Este es el patrón "Offline-First" o "Base de datos como única fuente de verdad".
     *
     * 1. Devolvemos INMEDIATAMENTE lo que hay en la base de datos local (localDataSource.observe()).
     *    Así la UI muestra datos instantáneamente (aunque sean viejos).
     *
     * 2. En segundo plano (scope.launch), llamamos a 'refresh()' para actualizar los datos desde internet.
     */
    override fun observe(): Flow<Result<List<Pokemon>>> {
        scope.launch {
            refresh() // Dispara la actualización en background
        }
        // La UI solo escucha a la BD. Si refresh() actualiza la BD, este Flow emitirá los nuevos datos automáticamente.
        return localDataSource.observe()
    }

    /**
     * Lógica de sincronización.
     * 1. Descarga de internet.
     * 2. Si hay éxito, guarda en local.
     */
    private suspend fun refresh() {
        val resultRemotePokemon = remoteDataSource.readAll()
        if (resultRemotePokemon.isSuccess) {
            // Guardar en local disparará el Flow de 'observe()'
            localDataSource.addAll(resultRemotePokemon.getOrNull()!!)
        }
    }
}