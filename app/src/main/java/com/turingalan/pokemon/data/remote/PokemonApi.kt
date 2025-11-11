package com.turingalan.pokemon.data.remote

import com.turingalan.pokemon.data.remote.model.PokemonListRemote
import com.turingalan.pokemon.data.remote.model.PokemonRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {
    @GET("/api/v2/pokemon/")
    suspend fun readAll(): Response<PokemonListRemote>

    @GET("/api/v2/pokemon/{id}")
    suspend fun readOne(@Path("id") id: Long): Response<PokemonRemote>

    @GET("/api/v2/pokemon/{name}")
    suspend fun readOne(@Path("name") name: String): Response<PokemonRemote>
}


