package com.turingalan.pokemon.di

import com.turingalan.pokemon.data.remote.PokemonApi
import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.data.remote.PokemonRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun providePokemonApi(): PokemonApi{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PokemonApi::class.java)
    }

    @Provides

    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

}