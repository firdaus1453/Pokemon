package com.example.pokemon.auth.data

import com.example.pokemon.auth.domain.AuthRepository
import com.example.pokemon.core.domain.AuthInfo
import com.example.pokemon.core.domain.SessionStorage
import com.example.pokemon.core.domain.auth.LocalUserDataSource
import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.EmptyResult
import com.example.pokemon.core.domain.util.Result
import java.util.UUID

class AuthRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        val exists = localUserDataSource.userExists(email)
        if (!exists) {
            return Result.Error(DataError.Network.UNAUTHORIZED)
        }

        val authInfo = AuthInfo(
            accessToken = generateDummyToken(),
            refreshToken = generateDummyToken(),
            userId = email
        )
        sessionStorage.set(authInfo)

        return Result.Success(Unit)
    }

    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        if (localUserDataSource.userExists(email)) {
            return Result.Error(DataError.Network.CONFLICT)
        }

        val userId = email
        val passwordHash = hashPassword(password)

        val insertResult = localUserDataSource.insertUser(
            id = userId,
            email = email,
            passwordHash = passwordHash
        )

        when (insertResult) {
            is Result.Error -> {
                val mapped = when (insertResult.error) {
                    DataError.Local.DISK_FULL -> DataError.Network.SERVER_ERROR
                }
                return Result.Error(mapped)
            }
            is Result.Success -> {
                val authInfo = AuthInfo(
                    accessToken = generateDummyToken(),
                    refreshToken = generateDummyToken(),
                    userId = userId
                )
                sessionStorage.set(authInfo)
                return Result.Success(Unit)
            }
        }
    }

    private fun hashPassword(password: String): String {
        return password.reversed()
    }

    private fun generateDummyToken(): String {
        return UUID.randomUUID().toString()
    }
}
