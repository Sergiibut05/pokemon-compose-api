
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

class PokemonRemoteDataSource @Inject constructor(
    private val api: PokemonApi,
    private val scope: CoroutineScope
): PokemonDataSource {
    override suspend fun addAll(pokemonList: List<Pokemon>) {
        TODO("Not yet implemented")
    }

    override fun observe(): Flow<Result<List<Pokemon>>> {
        return flow {
            //
            emit(Result.success(listOf<Pokemon>()))
            val result = readAll()
            emit(result)
        }.shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000L),
            replay = 1
        )
    }

    override suspend fun readAll(): Result<List<Pokemon>> {
        //TODO REMOVE CODIGO MALO
        val response = api.readAll(limit = 20, offset = 0)
        val finalList = mutableListOf<Pokemon>()
        return if (response.isSuccessful) {
            val body = response.body()!!
            for (result in body.results) {
                val remotePokemon = readOne(name = result.name)
                remotePokemon?.let {
                    finalList.add(it)
                }
            }
            Result.success(finalList)
        }
        else {
            val status = response.code() //tipo de error
            Result.failure(RuntimeException())
        }
    }

    private suspend fun readOne(name: String): Pokemon? {
        val response = api.readOne(name)
        return if (response.isSuccessful) {
            response.body()!!.toExternal()
        }
        else {
            null
        }
    }

    override suspend fun readOne(id: Long): Result<Pokemon> {
        val response = api.readOne(id)
        return if (response.isSuccessful) {
            val pokemon = response.body()!!.toExternal()
            Result.success(pokemon)
        }
        else {
            Result.failure(RuntimeException())
        }
    }
}

fun PokemonRemote.toExternal(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        sprite = this.sprites.front_default,
        artwork = this.sprites.other.officialArtwork.front_default
    )
}
