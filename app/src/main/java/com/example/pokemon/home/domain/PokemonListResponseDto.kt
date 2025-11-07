package com.example.pokemon.home.domain

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemDto>
)

@Serializable
data class PokemonListItemDto(
    val name: String,
    val url: String
)
