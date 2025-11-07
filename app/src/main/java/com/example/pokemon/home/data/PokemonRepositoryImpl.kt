package com.example.pokemon.home.data

import com.example.pokemon.core.data.networking.safeCall
import com.example.pokemon.core.database.dao.PokemonDao
import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.Result
import com.example.pokemon.home.data.mappers.toDomain
import com.example.pokemon.home.data.mappers.toEntity
import com.example.pokemon.home.data.network.PokemonApiService
import com.example.pokemon.home.domain.Pokemon
import com.example.pokemon.home.domain.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val apiService: PokemonApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override fun getPokemonList(): Flow<List<Pokemon>> {
        return pokemonDao.getAllPokemon().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun fetchAndCachePokemon(
        limit: Int,
        offset: Int
    ): Result<Unit, DataError.Network> {
        // Fetch list
        val listResult = safeCall { apiService.getPokemonList(limit, offset) }

        if (listResult is Result.Error) {
            return Result.Error(listResult.error)
        }

        val pokemonList = (listResult as Result.Success).data.results

        // Fetch details for each pokemon
        val pokemonEntities = coroutineScope {
            pokemonList.map { item ->
                async {
                    val id = item.url.trimEnd('/').split("/").last().toInt()
                    val detailResult = safeCall { apiService.getPokemonDetail(id) }

                    if (detailResult is Result.Success) {
                        detailResult.data.toEntity()
                    } else {
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }

        // Cache to database
        pokemonDao.insertPokemons(pokemonEntities)

        return Result.Success(Unit)
    }

    override suspend fun getPokemonDetail(id: Int): Pokemon? {
        return pokemonDao.getPokemonById(id)?.toDomain()
    }
}