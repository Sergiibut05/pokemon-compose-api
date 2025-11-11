package com.turingalan.pokemon.data.remote

import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.remote.model.PokemonRemote
import javax.inject.Inject

class PokemonRemoteDataSource
@Inject constructor(
    private val api: PokemonApi
): PokemonDataSource{
    override suspend fun readAll(): List<Pokemon> {
        val response = api.readAll()
        val finalList = mutableListOf<Pokemon>()
        return if (response.isSuccessful) {
            val body = response.body()!!
            for (result in body.results) {
                val remotePokemon = readOne(name = result.name)
                remotePokemon?.let {
                    finalList.add(it)
                }
            }
            finalList

        }
        else{
            listOf<Pokemon>()
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

    override suspend fun readOne(id: Long): Pokemon? {
        val response = api.readOne(id)
        return if (response.isSuccessful) {
            response.body()!!.toExternal()
        }
        else {
            null
        }
    }

}

fun PokemonRemote.toExternal():Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        sprite = this.sprites.front_default,
        artwork = this.sprites.other.officialArtwork.front_default
    )
}