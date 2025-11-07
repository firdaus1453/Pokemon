package com.example.pokemon.home.domain

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: SpritesDto,
    val types: List<TypeSlotDto>,
    val stats: List<StatDto>,
    val abilities: List<AbilitySlotDto>
)

@Serializable
data class SpritesDto(
    val other: OtherSpritesDto
)

@Serializable
data class OtherSpritesDto(
    val `official-artwork`: OfficialArtworkDto
)

@Serializable
data class OfficialArtworkDto(
    val front_default: String?
)

@Serializable
data class TypeSlotDto(
    val slot: Int,
    val type: TypeDto
)

@Serializable
data class TypeDto(
    val name: String
)

@Serializable
data class StatDto(
    val base_stat: Int,
    val stat: StatNameDto
)

@Serializable
data class StatNameDto(
    val name: String
)

@Serializable
data class AbilitySlotDto(
    val ability: AbilityDto,
    val is_hidden: Boolean
)

@Serializable
data class AbilityDto(
    val name: String
)
