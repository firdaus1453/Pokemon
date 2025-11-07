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
        val pokemonId: Int
    ) : Routes
}