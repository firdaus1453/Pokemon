package com.example.pokemon.home.domain

import androidx.compose.ui.graphics.Color

data class Pokemon(
    val number: Int,
    val name: String,
    val types: List<String>,
    val imageUrl: String,
    val backgroundColor: Color
)