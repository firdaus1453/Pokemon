package com.example.pokemon.profile.presentation.di

import com.example.pokemon.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val profileViewModelModule = module {
    viewModelOf(::ProfileViewModel)
}
