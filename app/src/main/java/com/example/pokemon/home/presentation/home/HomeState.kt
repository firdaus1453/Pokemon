package com.example.pokemon.home.presentation.home

data class HomeState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val pokemonList: List<com.example.pokemon.home.domain.Pokemon> = emptyList(),
    val error: String? = null,
    val canLoadMore: Boolean = true
)