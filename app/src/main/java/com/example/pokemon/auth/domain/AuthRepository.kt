package com.example.pokemon.auth.domain

import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
}