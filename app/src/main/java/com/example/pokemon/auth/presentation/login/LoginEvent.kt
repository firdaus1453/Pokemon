package com.example.pokemon.auth.presentation.login

import com.example.pokemon.core.presentation.ui.UiText

sealed interface LoginEvent {
    data class Error(val error: UiText): LoginEvent
    data object LoginSuccess: LoginEvent
}