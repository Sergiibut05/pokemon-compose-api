package com.turingalan.pokemon.data.repository

import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.remote.PokemonDataSource
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val dataSource: PokemonDataSource
): PokemonRepository {
    override suspend fun readOne(id: Long): Pokemon? {
        return dataSource.readOne(id)
    }

    override suspend fun readAll(): List<Pokemon> {
        return dataSource.readAll()
    }
}