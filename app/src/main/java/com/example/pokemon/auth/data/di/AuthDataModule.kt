package com.example.pokemon.auth.data.di

import com.example.pokemon.auth.data.AuthRepositoryImpl
import com.example.pokemon.auth.data.EmailPatternValidator
import com.example.pokemon.auth.domain.AuthRepository
import com.example.pokemon.auth.domain.PatternValidator
import com.example.pokemon.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}
