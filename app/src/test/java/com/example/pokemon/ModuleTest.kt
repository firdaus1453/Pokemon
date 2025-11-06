package com.example.pokemon

import com.example.pokemon.auth.data.di.authDataModule
import com.example.pokemon.auth.presentation.di.authViewModelModule
import com.example.pokemon.core.di.databaseModule
import com.example.pokemon.di.appModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify

class ModuleTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkKoinModule() {
        val allModule = module {
            includes(
                appModule,
                databaseModule,
                authDataModule,
                authViewModelModule
            )
        }
        allModule.verify()
    }
}