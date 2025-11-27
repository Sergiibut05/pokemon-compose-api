package com.turingalan.pokemon.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.turingalan.pokemon.data.model.Pokemon

/**
 * [CAPA DE DATOS - LOCAL]
 *
 * Esta clase representa una TABLA en tu base de datos SQLite (usando Room).
 *
 * @Entity(tableName = "pokemon"): Le dice a Room que cree una tabla llamada "pokemon".
 * Cada instancia de esta clase será una FILA en esa tabla.
 *
 * NOTA: Esta clase SOLO debe usarse dentro de la capa 'data/local'. Nunca debe llegar a la UI.
 * Para eso tenemos los "Mappers" (funciones de conversión) al final del archivo.
 */
@Entity(tableName = "pokemon")
data class PokemonEntity(
    /**
     * @PrimaryKey: Indica que este campo es el identificador único de la fila.
     * No puede haber dos Pokémons con el mismo ID.
     */
    @PrimaryKey
    val id: Long,
    val name: String,
    val sprite: String,
    val artwork: String
)

/**
 * [MAPPERS]
 * Estas funciones de extensión son vitales en Clean Architecture.
 * Transforman los datos de un formato "sucio" (Base de datos) a un formato "limpio" (Dominio) y viceversa.
 */

/**
 * Convierte tu objeto de Dominio (Pokemon) a una Entidad de Base de Datos (PokemonEntity).
 * Se usa cuando quieres GUARDAR datos en la BD.
 */
fun Pokemon.toEntity(): PokemonEntity {
    return PokemonEntity(
        id = this.id,
        name = this.name,
        sprite = this.sprite,
        artwork = this.artwork
    )
}

/**
 * Convierte una lista de Entidades (lo que sale de la BD) a una lista de objetos de Dominio (Pokemon).
 * Se usa cuando LEES datos de la BD para mostrarlos en la UI.
 */
fun List<PokemonEntity>.toModel(): List<Pokemon> {
    // 'map' recorre cada elemento de la lista y lo transforma.
    return this.map {
        Pokemon(
            id = it.id,
            name = it.name,
            sprite = it.sprite,
            artwork = it.artwork
        )
    }
}

/**
 * Convierte una sola Entidad a un objeto de Dominio.
 */
fun PokemonEntity.toModel(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        sprite = this.sprite,
        artwork = this.artwork
    )
}