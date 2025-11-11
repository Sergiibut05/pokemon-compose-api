package com.turingalan.pokemon.data.remote.model

import com.google.gson.annotations.SerializedName
import com.turingalan.pokemon.data.model.Pokemon

data class PokemonListRemote(
    val results:List<PokemonListItemRemote>
)

data class PokemonListItemRemote(

    val name: String,
    val url: String,
)

data class PokemonRemote(
    val id:Long,
    val name: String,
    val sprites: PokemonSprites,
)
data class PokemonOfficialArtwork(
    val front_default: String
)
data class PokemonSprites(
    val front_default: String,
    var other: PokemonOtherSprites,
)

data class PokemonOtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: PokemonOfficialArtwork,

)
