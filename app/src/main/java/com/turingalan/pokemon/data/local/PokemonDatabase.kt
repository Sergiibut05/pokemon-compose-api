package com.turingalan.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PokemonEntity::class], version = 1)
abstract class PokemonDatabase(): RoomDatabase() {

    abstract fun getPokemonDao(): PokemonDao
}