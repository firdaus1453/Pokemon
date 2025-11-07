package com.example.pokemon.home.presentation.di

import com.example.pokemon.home.presentation.home.HomeViewModel
import com.example.pokemon.home.presentation.home_detail.HomeDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModelOf(::HomeViewModel)

    viewModel { parameters ->
        HomeDetailViewModel(
            pokemonRepository = get(),
            pokemonId = parameters.get()
        )
    }
}