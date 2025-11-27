package com.turingalan.pokemon.data.remote.model

import com.google.gson.annotations.SerializedName
import com.turingalan.pokemon.data.model.Pokemon

/**
 * [CAPA DE DATOS - REMOTA - MODELOS (DTOs)]
 *
 * Estas clases son "espejos" de la respuesta JSON que envía la API de PokeAPI.co.
 *
 * Regla de Oro: NUNCA uses estas clases en tu UI o en tu lógica de negocio.
 * Úsalas solo para recibir el JSON y luego conviértelas a tu modelo limpio 'Pokemon' (usando un mapper).
 *
 * ¿Por qué?
 * Si la API cambia mañana y renombra "sprites" a "images", solo tendrás que cambiar este archivo
 * y el mapper. El resto de tu app (ViewModels, UI, Base de datos) ni se enterará.
 */

/**
 * Representa la respuesta de la lista de Pokémons (/pokemon/).
 * El JSON se ve así:
 * {
 *    "results": [ ... ]
 * }
 */
data class PokemonListRemote(
    val results: List<PokemonListItemRemote>
)

/**
 * Elemento simple de la lista.
 */
data class PokemonListItemRemote(
    val name: String,
    val url: String,
)

/**
 * Representa el detalle completo de un Pokémon (/pokemon/{id}).
 * El JSON es enorme, pero aquí solo definimos los campos que nos interesan.
 * Retrofit ignorará el resto.
 */
data class PokemonRemote(
    val id: Long,
    val name: String,
    val sprites: PokemonSprites, // Objeto anidado
)

/**
 * Estructura anidada para llegar a la imagen.
 * JSON: "sprites": { "front_default": "url...", "other": { ... } }
 */
data class PokemonSprites(
    val front_default: String,
    var other: PokemonOtherSprites,
)

data class PokemonOtherSprites(
    // @SerializedName: Nos permite usar nombres de variables Kotlin bonitos (camelCase)
    // aunque el JSON venga con guiones o nombres raros (kebab-case o snake_case).
    @SerializedName("official-artwork")
    val officialArtwork: PokemonOfficialArtwork,
)

data class PokemonOfficialArtwork(
    val front_default: String
)