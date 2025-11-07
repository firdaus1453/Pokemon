package com.example.pokemon.home.presentation.home

import com.example.pokemon.home.domain.Pokemon

sealed interface HomeAction {
    data class OnItemClick(val idPokemon: Int): HomeAction
    data object OnLoadMore: HomeAction
    data object OnRefresh: HomeAction
}