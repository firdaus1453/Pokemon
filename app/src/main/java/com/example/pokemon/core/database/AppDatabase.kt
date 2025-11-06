package com.example.pokemon.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemon.core.database.dao.UserDao
import com.example.pokemon.core.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}