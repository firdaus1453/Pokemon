package com.example.pokemon

import android.app.Application
import com.example.pokemon.auth.data.di.authDataModule
import com.example.pokemon.auth.presentation.di.authViewModelModule
import com.example.pokemon.core.di.databaseModule
import com.example.pokemon.profile.data.di.profileDataModule
import com.example.pokemon.profile.presentation.di.profileViewModelModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import com.example.pokemon.di.appModule
import com.example.pokemon.home.data.di.homeDataModule
import com.example.pokemon.home.presentation.di.homeViewModelModule

class PokemonApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@PokemonApp)
            modules(
                appModule,
                databaseModule,
                authDataModule,
                authViewModelModule,
                profileDataModule,
                profileViewModelModule,
                homeDataModule,
                homeViewModelModule
            )
        }
    }
}
