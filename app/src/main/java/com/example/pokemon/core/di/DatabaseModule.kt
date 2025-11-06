package com.example.pokemon.core.di

import androidx.room.Room
import com.example.pokemon.core.database.AppDatabase
import com.example.pokemon.core.database.RoomLocalUserDataSource
import com.example.pokemon.core.domain.SessionStorage
import com.example.pokemon.core.domain.auth.LocalUserDataSource
import com.example.pokemon.core.data.auth.EncryptedSessionStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    single { get<AppDatabase>().userDao }

    singleOf(::RoomLocalUserDataSource).bind<LocalUserDataSource>()
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}
