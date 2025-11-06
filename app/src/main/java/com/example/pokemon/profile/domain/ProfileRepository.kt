package com.example.pokemon.profile.domain

import com.example.pokemon.core.database.entity.UserEntity

interface ProfileRepository {
    suspend fun getCurrentUser(): UserEntity?
    suspend fun logout()
}
