package com.turingalan.pokemon.data.local

/**
 * [EXCEPCIONES PERSONALIZADAS]
 *
 * En lugar de lanzar un "RuntimeException" genérico o dejar que la app falle con un "NullPointerException",
 * creamos nuestras propias excepciones.
 *
 * Beneficio: En el ViewModel o en la UI podemos preguntar específicamente:
 * "if (error is PokemonNotFoundException) { mostrarMensaje("Pokémon no encontrado") }"
 *
 * Esto hace el manejo de errores mucho más limpio y semántico.
 */
class PokemonNotFoundException : RuntimeException() {
}