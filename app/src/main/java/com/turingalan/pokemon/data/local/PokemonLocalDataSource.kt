package com.turingalan.pokemon.data.local

import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.data.model.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PokemonLocalDataSource @Inject constructor(
    private val scope: CoroutineScope
): PokemonDataSource {

    private val _pokemon: MutableList<Pokemon> = mutableListOf()
    private val _pokemonFlow: MutableSharedFlow<Result<List<Pokemon>>> = MutableSharedFlow()

    override suspend fun addAll(pokemonList: List<Pokemon>) {
        _pokemon.addAll(pokemonList)
        _pokemonFlow.emit(Result.success(_pokemon.toList()))
    }

    override fun observe(): Flow<Result<List<Pokemon>>> {
        scope.launch {
            val success = Result.success(_pokemon)
            _pokemonFlow.emit(success)
        }
        return _pokemonFlow
    }

    override suspend fun readAll(): Result<List<Pokemon>> {
        return Result.success(_pokemon.toList())
    }

    override suspend fun readOne(id: Long): Result<Pokemon> {
        val pokemon = _pokemon.firstOrNull { pokemon ->
            pokemon.id == id
        }
        pokemon?.let {
            return Result.success(it)
        }
        return Result.failure(RuntimeException())
    }
}