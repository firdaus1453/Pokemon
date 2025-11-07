package com.example.pokemon.home.presentation.home_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.home.domain.PokemonRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeDetailViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokemonId: Int
) : ViewModel() {

    var state by mutableStateOf(HomeDetailState())
        private set

    private val eventChannel = Channel<HomeDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadPokemonDetail()
    }

    fun onAction(action: HomeDetailAction) {
        when(action) {
            HomeDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeDetailEvent.NavigateBack)
                }
            }
        }
    }

    private fun loadPokemonDetail() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val pokemon = pokemonRepository.getPokemonDetail(pokemonId)

            state = if (pokemon != null) {
                state.copy(
                    isLoading = false,
                    pokemon = pokemon
                )
            } else {
                state.copy(
                    isLoading = false,
                    error = "Pokemon not found"
                )
            }
        }
    }
}
