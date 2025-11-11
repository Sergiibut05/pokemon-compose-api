package com.turingalan.pokemon.data.repository

import com.turingalan.pokemon.data.model.Pokemon

interface PokemonRepository {

    suspend fun readOne(id:Long): Pokemon?
    suspend fun readAll():List<Pokemon>
}