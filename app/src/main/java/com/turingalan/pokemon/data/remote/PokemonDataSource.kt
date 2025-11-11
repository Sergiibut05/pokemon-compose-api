package com.turingalan.pokemon.data.remote

import com.turingalan.pokemon.data.model.Pokemon

interface PokemonDataSource {

    suspend fun readAll():List<Pokemon>
    suspend fun readOne(id:Long): Pokemon?
}