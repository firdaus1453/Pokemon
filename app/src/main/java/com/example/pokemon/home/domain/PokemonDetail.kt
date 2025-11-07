package com.example.pokemon.home.domain

import androidx.compose.ui.graphics.Color

data class PokemonDetail(
    val number: Int,
    val name: String,
    val types: List<String>,
    val imageUrl: String,
    val backgroundColor: Color,
    val height: Int,
    val weight: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
    val abilities: List<String>,
    val category: String
)