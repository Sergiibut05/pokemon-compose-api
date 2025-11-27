package com.turingalan.pokemon.data.local

import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.data.model.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [CAPA DE DATOS - LOCAL - DATA SOURCE]
 *
 * Esta clase implementa la interfaz genérica 'PokemonDataSource' pero usando la BASE DE DATOS local.
 * Su trabajo es coordinar al DAO y hacer transformaciones de datos (Entity <-> Model).
 *
 * @Inject: Inyección de Dependencias (Hilt).
 * Le dice a Hilt: "Cuando alguien pida un PokemonLocalDataSource, usa este constructor para crearlo".
 * Hilt inyectará automáticamente el 'PokemonDao' y el 'CoroutineScope'.
 */
class PokemonLocalDataSource @Inject constructor(
    private val scope: CoroutineScope, // Ámbito para lanzar corrutinas (no se usa mucho aquí porque usamos suspend)
    private val pokemonDao: PokemonDao // Nuestra conexión con la BD
): PokemonDataSource {

    /**
     * Guarda una lista de Pokémons en la base de datos.
     */
    override suspend fun addAll(pokemonList: List<Pokemon>) {
        // Iteramos sobre la lista de Pokémons recibida
        pokemonList.forEach { pokemon ->
            // 1. Convertimos el modelo de Dominio (Pokemon) a Entidad de BD (PokemonEntity)
            val entity = pokemon.toEntity()

            /**
             * withContext(Dispatchers.IO): CAMBIO DE HILO.
             *
             * - Main: Hilo principal (UI). Dibujar cosas, animaciones. ¡Nunca bloquear!
             * - IO (Input/Output): Hilo para operaciones de Entrada/Salida (Base de datos, Red, Archivos).
             * - Default: Hilo para cálculos pesados de CPU (algoritmos complejos).
             *
             * Aquí nos aseguramos explícitamente de que la inserción ocurra en el hilo IO para no congelar la app.
             */
            withContext(Dispatchers.IO) {
                pokemonDao.insert(entity)
            }
        }
    }

    /**
     * Observa los cambios en la base de datos.
     * Retorna un Flow<Result<List<Pokemon>>>.
     */
    override fun observe(): Flow<Result<List<Pokemon>>> {
        // 1. Obtenemos el Flow de Entidades del DAO.
        val databaseFlow = pokemonDao.observeAll()

        // 2. Transformamos (map) ese Flow.
        // Cada vez que la BD emite una nueva lista de Entidades, nosotros la convertimos a una lista de Modelos
        // y la envolvemos en un Result.success().
        return databaseFlow.map { entities ->
            Result.success(entities.toModel())
        }
    }

    /**
     * Lee todos los datos una sola vez (snapshot).
     */
    override suspend fun readAll(): Result<List<Pokemon>> {
        // Llamamos al DAO, convertimos a modelo y devolvemos éxito.
        // Si el DAO fallara, la excepción subiría y debería capturarse (aquí falta un try-catch idealmente).
        val result = Result.success(pokemonDao.getAll().toModel())
        return result
    }

    /**
     * Lee un solo Pokémon por ID.
     */
    override suspend fun readOne(id: Long): Result<Pokemon> {
        val entity = pokemonDao.readPokemonById(id)

        // Verificamos si se encontró algo
        return if(entity == null){
            // Si es null, devolvemos un error personalizado
            Result.failure(PokemonNotFoundException())
        }
        else
            // Si existe, lo convertimos a modelo y devolvemos éxito
            Result.success(entity.toModel())
    }

    // Esta función parece no usarse en local, por eso lanza una excepción o no hace nada.
    // En una interfaz genérica, a veces no todos los métodos tienen sentido para todas las implementaciones.
    override suspend fun isError() {
        TODO("Not yet implemented")
    }
}