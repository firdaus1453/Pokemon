package com.example.pokemon.home.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

import androidx.lifecycle.viewModelScope
import com.example.pokemon.core.domain.util.Result
import com.example.pokemon.home.domain.PokemonRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val eventHome = Channel<HomeEvent>()
    val events = eventHome.receiveAsFlow()

    private var currentOffset = 0
    private val limit = 10

    init {
        loadPokemonList()
        observePokemonList()
    }

    fun onAction(action: HomeAction) {
        when(action) {
            is HomeAction.OnItemClick -> Unit
            HomeAction.OnLoadMore -> loadMore()
            HomeAction.OnRefresh -> refresh()
            is HomeAction.OnSearch -> search(action.query)
            HomeAction.OnClearSearch -> clearSearch()
        }
    }

    private fun observePokemonList() {
        pokemonRepository.getPokemonList()
            .onEach { pokemonList ->
                state = state.copy(
                    pokemonList = pokemonList,
                    isLoading = false,
                    isLoadingMore = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = pokemonRepository.fetchAndCachePokemon(limit, currentOffset)

            if (result is Result.Success) {
                currentOffset += limit
            } else {
                state = state.copy(
                    isLoading = false,
                    error = "Failed to load Pokemon"
                )
            }
        }
    }

    private fun loadMore() {
        if (state.isLoadingMore || !state.canLoadMore) return

        viewModelScope.launch {
            state = state.copy(isLoadingMore = true)
            val result = pokemonRepository.fetchAndCachePokemon(limit, currentOffset)

            if (result is Result.Success) {
                currentOffset += limit
            } else {
                state = state.copy(isLoadingMore = false)
            }
        }
    }

    private fun refresh() {
        currentOffset = 0
        state = state.copy(pokemonList = emptyList())
        loadPokemonList()
    }

    private fun search(query: String) {
        if (query.isBlank()) {
            clearSearch()
            return
        }

        viewModelScope.launch {
            state = state.copy(
                isSearching = true,
                searchQuery = query,
                searchError = null
            )

            val result = pokemonRepository.searchPokemon(query.trim())

            when (result) {
                is Result.Success -> {
                    state = state.copy(
                        isSearching = false,
                        searchResults = result.data,
                        searchError = if (result.data.isEmpty()) "No Pokemon found" else null
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isSearching = false,
                        searchResults = emptyList(),
                        searchError = "Pokemon not found. Please check the name or try another search."
                    )
                }
            }
        }
    }

    private fun clearSearch() {
        state = state.copy(
            isSearching = false,
            searchResults = emptyList(),
            searchQuery = "",
            searchError = null
        )
    }
}