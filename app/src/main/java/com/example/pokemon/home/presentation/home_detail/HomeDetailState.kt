package com.example.pokemon.home.presentation.home_detail

import com.example.pokemon.home.domain.PokemonDetail

data class HomeDetailState(
    val isLoading: Boolean = true,
    val pokemon: PokemonDetail? = null,
    val error: String? = null
)
