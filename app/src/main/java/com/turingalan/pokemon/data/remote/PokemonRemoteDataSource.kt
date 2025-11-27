package com.turingalan.pokemon.data.remote

import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.remote.model.PokemonRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

/**
 * [CAPA DE DATOS - REMOTA - DATA SOURCE]
 *
 * Esta clase implementa 'PokemonDataSource' usando la API (Internet).
 * Se encarga de llamar a la API, recibir los datos "crudos" (JSON/DTOs) y convertirlos al Dominio.
 */
class PokemonRemoteDataSource @Inject constructor(
    private val api: PokemonApi,
    private val scope: CoroutineScope
): PokemonDataSource {

    // No tiene sentido "añadir" Pokémons a la API en este ejemplo, así que lo dejamos vacío o lanzamos error.
    override suspend fun addAll(pokemonList: List<Pokemon>) {
        TODO("Not yet implemented")
    }

    /**
     * Una implementación básica de 'observe' para remoto.
     * Generalmente, el "Flow" real viene de la Base de Datos Local.
     * Aquí simulamos un Flow que emite una lista vacía y luego la lista de la red.
     */
    override fun observe(): Flow<Result<List<Pokemon>>> {
        return flow {
            // 1. Emitimos vacío al principio
            emit(Result.success(listOf<Pokemon>()))
            // 2. Llamamos a la red
            val result = readAll()
            // 3. Emitimos el resultado de la red
            emit(result)
        }.shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000L), // Mantiene el flow vivo 5 segundos después de que la UI deje de escuchar
            replay = 1 // Repite el último valor a nuevos suscriptores
        )
    }

    /**
     * Descarga la lista de Pokémons.
     */
    override suspend fun readAll(): Result<List<Pokemon>> {
        try {
            // 1. Llamada síncrona (pero dentro de una corrutina suspendida) a la API
            val response = api.readAll(limit = 20, offset = 0)
            val finalList = mutableListOf<Pokemon>()

            // 2. Verificamos si el servidor respondió 200 OK
            return if (response.isSuccessful) {
                val body = response.body()!! // Ojo con el !!, mejor usar ?.let o manejo seguro
                // La API de lista solo devuelve nombres, así que tenemos que pedir el detalle de CADA uno
                // para tener la foto (sprite). Esto es ineficiente (N+1 requests), pero es como funciona esta API.
                for (result in body.results) {
                    val remotePokemon = readOne(name = result.name)
                    remotePokemon?.let {
                        finalList.add(it)
                    }
                }
                Result.success(finalList)
            } else {
                // Error del servidor (ej. 404, 500)
                Result.failure(RuntimeException("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de conexión (ej. Sin internet)
            return Result.failure(e)
        }
    }

    /**
     * Función auxiliar para leer por nombre (usada dentro de readAll).
     */
    private suspend fun readOne(name: String): Pokemon? {
        val response = api.readOne(name)
        return if (response.isSuccessful) {
            response.body()!!.toExternal() // Mapper de Remote -> Domain
        } else {
            null
        }
    }

    /**
     * Lee un Pokémon por ID.
     */
    override suspend fun readOne(id: Long): Result<Pokemon> {
        try {
            val response = api.readOne(id)
            return if (response.isSuccessful) {
                val pokemon = response.body()!!.toExternal()
                Result.success(pokemon)
            } else {
                Result.failure(RuntimeException("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun isError() {
        TODO("Not yet implemented")
    }
}

/**
 * [MAPPER]
 * Convierte el objeto de la API (PokemonRemote) al objeto de Dominio (Pokemon).
 * Es fundamental para desacoplar la respuesta JSON de tu lógica de negocio.
 */
fun PokemonRemote.toExternal(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        sprite = this.sprites.front_default,
        artwork = this.sprites.other.officialArtwork.front_default
    )
}