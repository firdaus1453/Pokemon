package com.example.pokemon.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemon.core.database.dao.PokemonDao
import com.example.pokemon.core.database.dao.UserDao
import com.example.pokemon.core.database.entity.PokemonEntity
import com.example.pokemon.core.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PokemonEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val pokemonDao: PokemonDao
}