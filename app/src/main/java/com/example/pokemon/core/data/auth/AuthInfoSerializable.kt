package com.example.pokemon.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val userId: String,
    val accessToken: String,
    val refreshToken: String
)
