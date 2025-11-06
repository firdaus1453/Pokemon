package com.example.pokemon.profile.presentation

sealed interface ProfileEvent {
    data object LogoutSuccess : ProfileEvent
}
