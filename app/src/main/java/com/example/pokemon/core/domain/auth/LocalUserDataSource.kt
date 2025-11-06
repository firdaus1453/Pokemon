package com.example.pokemon.core.domain.auth

import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.Result

interface LocalUserDataSource {
    suspend fun userExists(email: String): Boolean
    suspend fun getUserId(email: String): String?
    suspend fun insertUser(
        id: String,
        email: String,
        name: String,
        passwordHash: String
    ): Result<Unit, DataError.Local>
}