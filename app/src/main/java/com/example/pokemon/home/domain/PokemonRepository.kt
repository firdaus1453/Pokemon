package com.example.pokemon.home.domain

import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<Pokemon>>
    suspend fun fetchAndCachePokemon(limit: Int, offset: Int): Result<Unit, DataError.Network>
    suspend fun getPokemonDetail(id: Int): PokemonDetail?
}