package com.turingalan.pokemon.data.repository

import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.di.LocalDataSource
import com.turingalan.pokemon.di.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    @RemoteDataSource private val remoteDataSource: PokemonDataSource,
    @LocalDataSource private val localDataSource: PokemonDataSource,
    private val scope: CoroutineScope
): PokemonRepository {
    override suspend fun readOne(id: Long): Result<Pokemon> {
        return remoteDataSource.readOne(id)
    }
    override suspend fun readAll(): Result<List<Pokemon>> {
        return remoteDataSource.readAll()
    }

    override fun observe(): Flow<Result<List<Pokemon>>> {
        scope.launch {
            refresh()
        }
        return localDataSource.observe()
    }

    private suspend fun refresh() {
        val resultRemotePokemon = remoteDataSource.readAll()
        if (resultRemotePokemon.isSuccess) {
            localDataSource.addAll(resultRemotePokemon.getOrNull()!!)
        }
    }
}