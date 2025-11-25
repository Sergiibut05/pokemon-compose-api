package com.turingalan.pokemon.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.turingalan.pokemon.data.model.Pokemon

@Entity("pokemon")
data class PokemonEntity(
    @PrimaryKey
    val id:Long,
    val name:String,
    val sprite:String?,
    val artwork:String?
)

fun Pokemon.toEntity(): PokemonEntity{
    return PokemonEntity(
        id = this.id,
        name = this.name,
        sprite = this.sprite,
        artwork = this.artwork
    )
}

fun PokemonEntity.toModel(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        sprite = this.sprite ?: "",
        artwork = this.artwork ?: ""
    )
}

fun List<PokemonEntity>.toModel():List<Pokemon> {
    return this.map(PokemonEntity::toModel)
}