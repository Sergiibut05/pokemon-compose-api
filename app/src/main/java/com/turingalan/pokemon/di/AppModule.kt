package com.turingalan.pokemon.di

import com.turingalan.pokemon.data.remote.PokemonDataSource
import com.turingalan.pokemon.data.remote.PokemonRemoteDataSource
import com.turingalan.pokemon.data.repository.PokemonRepository
import com.turingalan.pokemon.data.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun pokemonDataSource(ds: PokemonRemoteDataSource): PokemonDataSource

    @Binds
    @Singleton
    abstract  fun bindPokemonRepository(repository: PokemonRepositoryImpl): PokemonRepository

    //abstract fun bindPokemonRepository(repository: PokemonFakeRemoteRepository): PokemonRepository
    //abstract fun bindPokemonRepository(repository: PokemonInMemoryRepository): PokemonRepository
}