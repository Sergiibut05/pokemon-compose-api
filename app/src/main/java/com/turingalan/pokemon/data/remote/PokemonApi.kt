package com.turingalan.pokemon.data.remote

import androidx.compose.ui.geometry.Offset
import com.turingalan.pokemon.data.remote.model.PokemonListRemote
import com.turingalan.pokemon.data.remote.model.PokemonRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * [CAPA DE DATOS - REMOTA - API]
 *
 * Esta interfaz define cómo hablamos con el servidor (PokeAPI).
 * Retrofit leerá estas anotaciones y generará el código HTTP real.
 */
interface PokemonApi {

    /**
     * Pide una lista de Pokémons.
     * Endpoint: /api/v2/pokemon/
     *
     * @Query: Añade parámetros a la URL. Ejemplo: /pokemon/?limit=60&offset=0
     * @suspend: Al igual que con Room, las peticiones de red son lentas.
     *           Usamos 'suspend' para no bloquear la UI mientras esperamos la respuesta.
     *
     * Retorna: Response<PokemonListRemote>. 'Response' es una clase de Retrofit que nos dice si hubo éxito (200 OK) o error (404, 500).
     */
    @GET("/api/v2/pokemon/")
    suspend fun readAll(@Query("limit") limit:Int=60, @Query("offset") offset:Int=0): Response<PokemonListRemote>

    /**
     * Pide un Pokémon específico por ID.
     * @Path: Sustituye la parte {id} de la URL con el valor real.
     *        Ejemplo: readOne(25) -> /api/v2/pokemon/25
     */
    @GET("/api/v2/pokemon/{id}")
    suspend fun readOne(@Path("id") id: Long): Response<PokemonRemote>

    /**
     * Pide un Pokémon específico por nombre.
     * Ejemplo: readOne("pikachu") -> /api/v2/pokemon/pikachu
     */
    @GET("/api/v2/pokemon/{name}")
    suspend fun readOne(@Path("name") name: String): Response<PokemonRemote>
}