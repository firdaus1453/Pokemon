package com.example.pokemon.home.presentation.home_detail

sealed interface HomeDetailEvent {
    data object NavigateBack: HomeDetailEvent
}
