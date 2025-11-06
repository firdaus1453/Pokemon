package com.example.pokemon.profile.presentation

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val isEmailVisible: Boolean = false,
    val isLoading: Boolean = true,
    val isLoggingOut: Boolean = false
)
