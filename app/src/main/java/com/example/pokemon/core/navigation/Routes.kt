package com.example.pokemon.core.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object Auth : Routes

    @Serializable
    data object Register : Routes

    @Serializable
    data object Login : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data class DetailHome(
        val pokemonNumber: Int,
        val pokemonName: String,
        val pokemonTypes: String, // comma-separated types
        val pokemonImageUrl: String,
        val backgroundColor: Long // Color as Long (ARGB)
    ) : Routes
}