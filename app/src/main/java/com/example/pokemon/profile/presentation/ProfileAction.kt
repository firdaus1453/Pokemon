package com.example.pokemon.profile.presentation

sealed interface ProfileAction {
    data object OnToggleEmailVisibilityClick : ProfileAction
    data object OnLogoutClick : ProfileAction
}
