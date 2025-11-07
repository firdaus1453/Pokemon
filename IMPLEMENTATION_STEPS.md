# 📋 LANGKAH-LANGKAH IMPLEMENTASI POKEMON API

## LANGKAH 1: Buat Response DTOs untuk API

**File: `app/src/main/java/com/example/pokemon/home/data/dto/PokemonListResponseDto.kt`**
```kotlin
package com.example.pokemon.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemDto>
)

@Serializable
data class PokemonListItemDto(
    val name: String,
    val url: String
)
```

**File: `app/src/main/java/com/example/pokemon/home/data/dto/PokemonDetailDto.kt`**
```kotlin
package com.example.pokemon.home.data.dto

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
```

---

## LANGKAH 2: Buat Room Entity untuk Pokemon

**File: `app/src/main/java/com/example/pokemon/core/database/entity/PokemonEntity.kt`**
```kotlin
package com.example.pokemon.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val types: String, // comma-separated
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
    val abilities: String, // comma-separated
    val category: String
)
```

---

## LANGKAH 3: Update AppDatabase - Tambahkan PokemonEntity

**Update di: `app/src/main/java/com/example/pokemon/core/database/AppDatabase.kt`**
```kotlin
@Database(
    entities = [
        UserEntity::class,
        PokemonEntity::class  // TAMBAHKAN INI
    ],
    version = 2,  // UBAH VERSION
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val pokemonDao: PokemonDao  // TAMBAHKAN INI
}
```

---

## LANGKAH 4: Buat PokemonDao

**File: `app/src/main/java/com/example/pokemon/core/database/dao/PokemonDao.kt`**
```kotlin
package com.example.pokemon.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemon.core.database.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll()
}
```

---

## LANGKAH 5: Update DatabaseModule - Provide PokemonDao

**Update di: `app/src/main/java/com/example/pokemon/core/di/DatabaseModule.kt`**
```kotlin
// TAMBAHKAN ini di dalam module
single { get<AppDatabase>().pokemonDao }
```

---

## LANGKAH 6: Buat Retrofit API Service

**File: `app/src/main/java/com/example/pokemon/home/data/network/PokemonApiService.kt`**
```kotlin
package com.example.pokemon.home.data.network

import com.example.pokemon.home.data.dto.PokemonDetailDto
import com.example.pokemon.home.data.dto.PokemonListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponseDto>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): Response<PokemonDetailDto>
}
```

---

## LANGKAH 7: Buat Mapper dari DTO ke Domain

**File: `app/src/main/java/com/example/pokemon/home/data/mappers/PokemonMappers.kt`**
```kotlin
package com.example.pokemon.home.data.mappers

import androidx.compose.ui.graphics.Color
import com.example.pokemon.core.database.entity.PokemonEntity
import com.example.pokemon.home.data.dto.PokemonDetailDto
import com.example.pokemon.home.domain.Pokemon

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
```

---

## LANGKAH 8: Update Pokemon Domain Model

**Update di: `app/src/main/java/com/example/pokemon/home/domain/Pokemon.kt`**
Sudah ada, tidak perlu diubah.

---

## LANGKAH 9: Buat Repository Interface

**File: `app/src/main/java/com/example/pokemon/home/domain/PokemonRepository.kt`**
```kotlin
package com.example.pokemon.home.domain

import com.example.pokemon.core.domain.util.DataError
import com.example.pokemon.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<Pokemon>>
    suspend fun fetchAndCachePokemon(limit: Int, offset: Int): Result<Unit, DataError.Network>
    suspend fun getPokemonDetail(id: Int): Pokemon?
}
```

---

## LANGKAH 10: Implementasi Repository

**File: `app/src/main/java/com/example/pokemon/home/data/PokemonRepositoryImpl.kt`**
```kotlin
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
```

---

## LANGKAH 11: Buat Data Module untuk DI

**File: `app/src/main/java/com/example/pokemon/home/data/di/HomeDataModule.kt`**
```kotlin
package com.example.pokemon.home.data.di

import com.example.pokemon.home.data.PokemonRepositoryImpl
import com.example.pokemon.home.data.network.PokemonApiService
import com.example.pokemon.home.domain.PokemonRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val homeDataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApiService::class.java)
    }

    singleOf(::PokemonRepositoryImpl).bind<PokemonRepository>()
}
```

---

## LANGKAH 12: Update HomeState untuk Pagination

**File: `app/src/main/java/com/example/pokemon/home/presentation/home/HomeState.kt`**
```kotlin
package com.example.pokemon.home.presentation.home

data class HomeState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val pokemonList: List<com.example.pokemon.home.domain.Pokemon> = emptyList(),
    val error: String? = null,
    val canLoadMore: Boolean = true
)
```

---

## LANGKAH 13: Update HomeAction untuk Load More

**Update di: `app/src/main/java/com/example/pokemon/home/presentation/home/HomeAction.kt`**
```kotlin
package com.example.pokemon.home.presentation.home

import com.example.pokemon.home.domain.Pokemon

sealed interface HomeAction {
    data class OnItemClick(val pokemon: Pokemon): HomeAction
    data object OnLoadMore: HomeAction
    data object OnRefresh: HomeAction
}
```

---

## LANGKAH 14: Update HomeViewModel

**Update di: `app/src/main/java/com/example/pokemon/home/presentation/home/HomeViewModel.kt`**
```kotlin
package com.example.pokemon.home.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.core.domain.util.Result
import com.example.pokemon.home.domain.PokemonRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val eventHome = Channel<HomeEvent>()
    val events = eventHome.receiveAsFlow()

    private var currentOffset = 0
    private val limit = 10

    init {
        loadPokemonList()
        observePokemonList()
    }

    fun onAction(action: HomeAction) {
        when(action) {
            is HomeAction.OnItemClick -> Unit
            HomeAction.OnLoadMore -> loadMore()
            HomeAction.OnRefresh -> refresh()
        }
    }

    private fun observePokemonList() {
        pokemonRepository.getPokemonList()
            .onEach { pokemonList ->
                state = state.copy(
                    pokemonList = pokemonList,
                    isLoading = false,
                    isLoadingMore = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = pokemonRepository.fetchAndCachePokemon(limit, currentOffset)

            if (result is Result.Success) {
                currentOffset += limit
            } else {
                state = state.copy(
                    isLoading = false,
                    error = "Failed to load Pokemon"
                )
            }
        }
    }

    private fun loadMore() {
        if (state.isLoadingMore || !state.canLoadMore) return

        viewModelScope.launch {
            state = state.copy(isLoadingMore = true)
            val result = pokemonRepository.fetchAndCachePokemon(limit, currentOffset)

            if (result is Result.Success) {
                currentOffset += limit
            } else {
                state = state.copy(isLoadingMore = false)
            }
        }
    }

    private fun refresh() {
        currentOffset = 0
        state = state.copy(pokemonList = emptyList())
        loadPokemonList()
    }
}
```

---

## LANGKAH 15: Update HomeScreen untuk Real Data + Pagination

**Update di: `app/src/main/java/com/example/pokemon/home/presentation/home/HomeScreen.kt`**

**Import yang diperlukan:**
```kotlin
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.LaunchedEffect
```

**Update RootHomeScreen:**
```kotlin
@Composable
fun RootHomeScreen(
    modifier: Modifier = Modifier,
    onItemClick: (Pokemon) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    HomeScreen(
        modifier = modifier,
        state = viewModel.state,  // TAMBAHKAN INI
        onAction = { action ->
            when(action) {
                is HomeAction.OnItemClick -> onItemClick(action.pokemon)
            }
            viewModel.onAction(action)
        }
    )
}
```

**Update HomeScreen private function:**
```kotlin
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onAction: (HomeAction) -> Unit,
    state: HomeState  // TAMBAHKAN PARAMETER INI
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "What Pokémon are you\nlooking for?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Pokemon Grid
        if (state.isLoading && state.pokemonList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                val filteredList = state.pokemonList.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }

                items(
                    items = filteredList,
                    key = { it.number }
                ) { pokemon ->
                    PokemonCard(pokemon = pokemon, onAction = onAction)
                }

                // Load more indicator
                if (state.isLoadingMore) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                // Load more trigger
                if (!state.isLoadingMore && filteredList.isNotEmpty() && searchQuery.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        LaunchedEffect(Unit) {
                            onAction(HomeAction.OnLoadMore)
                        }
                    }
                }
            }
        }
    }
}

// HAPUS function getSamplePokemonList() - tidak diperlukan lagi
```

---

## LANGKAH 16: Register Modules di PokemonApp

**Update di: `app/src/main/java/com/example/pokemon/PokemonApp.kt`**
```kotlin
import com.example.pokemon.home.data.di.homeDataModule  // TAMBAHKAN

modules(
    appModule,
    databaseModule,
    authDataModule,
    authViewModelModule,
    profileDataModule,
    profileViewModelModule,
    homeDataModule,  // TAMBAHKAN
    homeViewModelModule
)
```

---

## LANGKAH 17: Add Dependencies (jika belum ada)

**Update di: `app/build.gradle.kts`** (di dependencies):
```kotlin
// Retrofit
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Gson (untuk serialization)
implementation("com.google.code.gson:gson:2.10.1")
```

---

## ✅ SELESAI!

Sekarang aplikasi Anda akan:
1. ✅ Fetch 10 Pokemon pertama saat pertama kali dibuka
2. ✅ Load 10 Pokemon berikutnya saat scroll ke bawah
3. ✅ Simpan semua data ke Room database untuk offline access
4. ✅ Menampilkan data real dari PokeAPI
5. ✅ Click item masuk ke detail screen dengan data yang sesuai

**Pattern yang diikuti:**
- Clean Architecture (Data, Domain, Presentation)
- Repository Pattern
- Room untuk local caching
- Retrofit untuk networking
- Koin untuk DI
- Flow untuk reactive data
