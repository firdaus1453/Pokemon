package com.example.pokemon.home.presentation.home

sealed interface HomeAction {
    data class OnItemClick(val idPokemon: Int): HomeAction
    data object OnLoadMore: HomeAction
    data object OnRefresh: HomeAction
    data class OnSearch(val query: String): HomeAction
    data object OnClearSearch: HomeAction
}