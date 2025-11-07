package com.example.pokemon.home.data.di

import com.example.pokemon.core.data.networking.HttpClientFactory
import com.example.pokemon.home.data.PokemonRepositoryImpl
import com.example.pokemon.home.data.network.PokemonApiService
import com.example.pokemon.home.domain.PokemonRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeDataModule = module {
    single<PokemonApiService> {
        get<HttpClientFactory>().build().create(PokemonApiService::class.java)
    }

    singleOf(::PokemonRepositoryImpl).bind<PokemonRepository>()
}