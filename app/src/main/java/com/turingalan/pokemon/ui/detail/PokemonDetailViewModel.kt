package com.turingalan.pokemon.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.repository.PokemonRepository
import com.turingalan.pokemon.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado específico para la pantalla de Detalle.
 * Solo necesitamos el nombre y la imagen grande (artwork).
 */
data class DetailUiState(
    val name:String = "",
    val artwork:String? = ""
)

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, // Aquí SÍ usamos savedStateHandle para leer el ID del Pokémon que nos pasaron al navegar.
    private val pokemonRepository: PokemonRepository

): ViewModel() {
    private val _uiState: MutableStateFlow<DetailUiState> =
        MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            /**
             * [NAVEGACIÓN TYPE-SAFE]
             * Usando la librería de navegación de Compose, podemos extraer los argumentos de forma segura.
             * 'Route.Detail' es una clase que definiste en tu configuración de navegación (probablemente data class Detail(val id: Long)).
             */
            val route = savedStateHandle.toRoute<Route.Detail>()
            val pokemonId = route.id

            // Pedimos al repositorio el Pokémon con ese ID.
            // Nota: Aquí usamos 'readOne' que en tu implementación actual va directo a la red.
            // En una app real, lo ideal sería 'observeOne(id)' para que si cambia en BD, se actualice aquí.
            val pokemonResult = pokemonRepository.readOne(pokemonId)

            // Verificamos si se obtuvo correctamente
            val pokemon = pokemonResult.getOrNull()
            pokemon?.let {
                // Transformamos a UiState y actualizamos la pantalla
                _uiState.value = it.toDetailUiState()
            }
        }
    }
}

/**
 * Mapper para convertir el modelo de Dominio al estado de la UI de detalle.
 */
fun Pokemon.toDetailUiState(): DetailUiState = DetailUiState(
    name = this.name,
    artwork = this.artwork,
)