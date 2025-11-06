package com.example.pokemon.core.database

import android.database.sqlite.SQLiteFullException
import com.example.pokemon.core.database.dao.UserDao
import com.example.pokemon.core.database.entity.UserEntity
import com.example.pokemon.core.domain.auth.LocalUserDataSource
import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.Result

class RoomLocalUserDataSource(
    private val userDao: UserDao
) : LocalUserDataSource {

    override suspend fun userExists(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    override suspend fun getUserId(email: String): String? {
        return userDao.getUserByEmail(email)?.id
    }

    override suspend fun insertUser(
        id: String,
        email: String,
        name: String,
        passwordHash: String
    ): Result<Unit, DataError.Local> {
        return try {
            val entity = UserEntity(
                id = id,
                email = email,
                name = name,
                passwordHash = passwordHash
            )
            userDao.insertUser(entity)
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }
}
