package com.turingalan.pokemon.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [VIEWMODEL - PRESENTADOR]
 *
 * El ViewModel es el intermediario entre la UI (Pantalla) y los Datos (Repositorio).
 * Sobrevive a cambios de configuración (como girar la pantalla), por lo que los datos no se pierden.
 *
 * @HiltViewModel: Permite que Hilt cree instancias de este ViewModel e inyecte dependencias.
 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, // Para recuperar argumentos de navegación (si los hubiera)
    private val repository: PokemonRepository // Inyectamos la Interfaz del repositorio (no la implementación)
): ViewModel() {

    /**
     * [ESTADO DE LA UI - STATEFLOW]
     *
     * _uiState (Mutable): Es privado para que SOLO el ViewModel pueda modificarlo.
     * uiState (Público): Es de solo lectura. La UI (Compose) observará esta variable.
     *
     * Cuando _uiState cambia, la UI se redibuja automáticamente.
     */
    private val _uiState: MutableStateFlow<ListUiState> =
        MutableStateFlow(value = ListUiState.Initial)
    val uiState: StateFlow<ListUiState>
        get() = _uiState.asStateFlow()

    /**
     * El bloque 'init' se ejecuta una sola vez cuando se crea el ViewModel.
     * Es el lugar perfecto para cargar los datos iniciales.
     */
    init {
        // viewModelScope: Es un ámbito de corrutina atado a la vida del ViewModel.
        // Si el usuario cierra la pantalla, esta corrutina se cancela automáticamente para ahorrar recursos.
        viewModelScope.launch {
            _uiState.value = ListUiState.Loading // Mostramos loading

            // Observamos el Flow del repositorio.
            // Cada vez que el repositorio emita nuevos datos (porque la BD cambió o se cargó internet),
            // este bloque 'collect' se ejecutará.
            repository.observe().collect { result ->
                if (result.isSuccess) {
                    // Si hay éxito, transformamos los datos de Dominio (Pokemon) a datos de UI (ListItemUiState)
                    val pokemons = result.getOrNull()!!
                    val uiPokemons = pokemons.asListUiState()
                    _uiState.value = ListUiState.Success(uiPokemons)
                } else {
                    // Si falló
                    _uiState.value = ListUiState.Error
                }
            }
        }
    }
}

/**
 * [ESTADOS DE LA UI]
 * Representan todas las posibles situaciones de la pantalla.
 * Sealed Class es como un Enum pero más potente (pueden llevar datos).
 */
sealed class ListUiState {
    object Initial: ListUiState()
    object Loading: ListUiState()
    object Error: ListUiState()
    data class Success(
        val pokemons: List<ListItemUiState>
    ): ListUiState()
}

/**
 * [MODELO DE UI]
 * A veces el modelo de Dominio (Pokemon) tiene más datos de los que la lista necesita (o menos).
 * Creamos un modelo específico para la UI para formatear datos (ej. mayúsculas, fechas, concatenaciones).
 */
data class ListItemUiState(
    val id: Long,
    val name: String,
    val sprite: String,
)

/**
 * [MAPPERS DE UI]
 * Transforman objetos de Dominio -> Objetos de UI.
 */
fun Pokemon.asListItemUiState(): ListItemUiState {
    return ListItemUiState(
        id = this.id,
        name = this.name.replaceFirstChar { it.uppercase() }, // Lógica de presentación: Primera letra mayúscula
        sprite = this.sprite
    )
}

fun List<Pokemon>.asListUiState(): List<ListItemUiState> = this.map(Pokemon::asListItemUiState)