package com.example.pokemon.profile.data.di

import com.example.pokemon.profile.data.ProfileRepositoryImpl
import com.example.pokemon.profile.domain.ProfileRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDataModule = module {
    singleOf(::ProfileRepositoryImpl).bind<ProfileRepository>()
}
