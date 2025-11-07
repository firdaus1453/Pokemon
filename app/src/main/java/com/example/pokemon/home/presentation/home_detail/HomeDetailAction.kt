package com.example.pokemon.home.presentation.home_detail

sealed interface HomeDetailAction {
    data object OnBackClick: HomeDetailAction
}
