package com.example.pokemon.home.data.mappers

import androidx.compose.ui.graphics.Color
import com.example.pokemon.core.database.entity.PokemonEntity
import com.example.pokemon.home.domain.Pokemon
import com.example.pokemon.home.domain.PokemonDetailDto

fun PokemonDetailDto.toEntity(): PokemonEntity {
    val hp = stats.find { it.stat.name == "hp" }?.base_stat ?: 0
    val attack = stats.find { it.stat.name == "attack" }?.base_stat ?: 0
    val defense = stats.find { it.stat.name == "defense" }?.base_stat ?: 0
    val specialAttack = stats.find { it.stat.name == "special-attack" }?.base_stat ?: 0
    val specialDefense = stats.find { it.stat.name == "special-defense" }?.base_stat ?: 0
    val speed = stats.find { it.stat.name == "speed" }?.base_stat ?: 0

    val typesList = types.sortedBy { it.slot }.map { it.type.name }
    val abilitiesList = abilities.filter { !it.is_hidden }.map { it.ability.name }

    return PokemonEntity(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        types = typesList.joinToString(","),
        imageUrl = sprites.other.`official-artwork`.front_default ?: "",
        height = height,
        weight = weight,
        hp = hp,
        attack = attack,
        defense = defense,
        specialAttack = specialAttack,
        specialDefense = specialDefense,
        speed = speed,
        abilities = abilitiesList.joinToString(","),
        category = "${name.replaceFirstChar { it.uppercase() }} Pokémon"
    )
}

fun PokemonEntity.toDomain(): Pokemon {
    return Pokemon(
        number = id,
        name = name,
        types = types.split(","),
        imageUrl = imageUrl,
        backgroundColor = getTypeColor(types.split(",").firstOrNull() ?: "normal")
    )
}

private fun getTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "grass" -> Color(0xFF48D0B0)
        "poison" -> Color(0xFFA040A0)
        "fire" -> Color(0xFFEE8130)
        "water" -> Color(0xFF6390F0)
        "electric" -> Color(0xFFF7D02C)
        "normal" -> Color(0xFFA8A878)
        "fairy" -> Color(0xFFEE99AC)
        "psychic" -> Color(0xFFF95587)
        "fighting" -> Color(0xFFC22E28)
        "flying" -> Color(0xFFA98FF3)
        "bug" -> Color(0xFFA6B91A)
        "rock" -> Color(0xFFB6A136)
        "ghost" -> Color(0xFF735797)
        "dragon" -> Color(0xFF6F35FC)
        "dark" -> Color(0xFF705746)
        "steel" -> Color(0xFFB7B7CE)
        "ice" -> Color(0xFF96D9D6)
        "ground" -> Color(0xFFE2BF65)
        else -> Color(0xFFA8A878)
    }
}