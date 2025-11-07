# Pokemon App

A modern Android application built with Clean Architecture principles, showcasing best practices in Android development. This app allows users to browse, search, and view detailed information about Pokemon.

## Screenshot
<img width="1080" height="1920" alt="Home" src="https://github.com/user-attachments/assets/568f3028-974d-4299-ae8b-ecaac9b26afa" hspace="20" style="width:30%; height:auto;"/>
<img width="1080" height="1920" alt="Detail" src="https://github.com/user-attachments/assets/d43d4f86-f76b-4cda-853f-d22f2f84ed71" hspace="20" style="width:30%; height:auto;"/>
</br>
</br>
<img width="1080" height="1920" alt="Stats" src="https://github.com/user-attachments/assets/6d944022-df46-4a98-ab32-a64900dbd543" hspace="20" style="width:30%; height:auto;"/>
<img width="1080" height="1920" alt="Profile" src="https://github.com/user-attachments/assets/4334d95e-6497-45f8-90f5-6f4311c758d1" hspace="20" style="width:30%; height:auto;"/>

## Features

- 📱 Browse Pokemon list with infinite scrolling
- 🔍 Smart search (local-first with API fallback)
- 📊 Detailed Pokemon information (stats, abilities, types)
- 💾 Offline-first architecture with local caching
- 🌄 Support image caching for offline mode
- 🔐 Secure authentication system
- 🎨 Using Design system custom

## Page :
1. Login
2. Register
3. Home
4. Detail Home
5. Profile

## Tech Stack

### Architecture & Design Patterns
- **Clean Architecture** - Separation of concerns with clear layer boundaries
- **MVVM + MVI Hybrid** - Unidirectional data flow with State-Action-Event pattern
- **Repository Pattern** - Single source of truth for data access
- **Dependency Injection** - Koin for loose coupling and testability

### Core Libraries
- **Jetpack Compose** - Modern declarative UI toolkit
- **Kotlin Coroutines & Flow** - Asynchronous programming and reactive streams
- **Room Database** - Local data persistence with type-safe queries
- **Retrofit + OkHttp** - Type-safe HTTP client for API communication
- **Navigation Compose** - Type-safe navigation with kotlinx.serialization
- **Coil** - Efficient image loading and caching

### Additional Libraries
- **kotlinx-serialization** - Kotlin-first JSON serialization
- **Security Crypto** - Encrypted SharedPreferences for sensitive data
- **Timber** - Extensible logging

## Architecture Overview

This project follows **Clean Architecture** principles with three distinct layers:

```
┌─────────────────────────────────────────────────┐
│              Presentation Layer                  │
│  (UI, ViewModels, States, Actions, Events)      │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│               Domain Layer                       │
│   (Use Cases, Repository Interfaces, Models)    │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│                Data Layer                        │
│  (Repository Impl, API, Database, Mappers)      │
└─────────────────────────────────────────────────┘
```

### Layer Responsibilities

#### 1. Presentation Layer
- **UI Components** - Jetpack Compose screens and components
- **ViewModels** - State management and business logic coordination
- **States** - Immutable UI state representations
- **Actions** - User interactions and intents
- **Events** - One-time UI events (navigation, toasts, etc.)

#### 2. Domain Layer
- **Repository Interfaces** - Contracts for data operations
- **Models** - Business entities (Pokemon, PokemonDetail)
- **Result & Error Types** - Type-safe error handling

#### 3. Data Layer
- **Repository Implementations** - Data access orchestration
- **API Services** - Retrofit interfaces for network calls
- **DAOs** - Room database access objects
- **Entities** - Database table representations
- **Mappers** - Conversion between layers (DTO ↔ Entity ↔ Domain)

## Project Structure

```
com.example.pokemon/
├── core/                          # Shared components across features
│   ├── data/                      # Core data utilities
│   │   ├── auth/                  # Authentication manager
│   │   └── networking/            # Network utilities (safeCall, HttpClientFactory)
│   ├── database/                  # Room database setup
│   │   ├── dao/                   # Data Access Objects
│   │   └── entity/                # Database entities
│   ├── domain/                    # Core domain models
│   │   ├── auth/                  # Auth interfaces
│   │   └── util/                  # Result & Error types
│   ├── navigation/                # Navigation setup & routes
│   └── presentation/              # Shared UI components & design system
│
├── auth/                          # Authentication feature
│   ├── data/                      # Auth data layer
│   ├── domain/                    # Auth domain layer
│   └── presentation/              # Login & Register screens
│
├── home/                          # Pokemon browsing feature
│   ├── data/                      # Pokemon data layer
│   │   ├── mappers/               # Data transformations
│   │   ├── network/               # API services
│   │   └── di/                    # Data module DI
│   ├── domain/                    # Pokemon domain models & repository interface
│   └── presentation/              # Pokemon UI
│       ├── home/                  # List screen (ViewModel, State, Actions)
│       ├── home_detail/           # Detail screen
│       └── di/                    # Presentation module DI
│
└── di/                            # App-level dependency injection
```

## Key Implementation Details

### Unidirectional Data Flow (MVI-style)

Each screen follows a predictable state management pattern:

```kotlin
// User interactions
sealed interface HomeAction {
    data class OnItemClick(val id: Int): HomeAction
    data class OnSearch(val query: String): HomeAction
}

// UI state
data class HomeState(
    val isLoading: Boolean = false,
    val pokemonList: List<Pokemon> = emptyList(),
    val searchResults: List<Pokemon> = emptyList()
)

// One-time events
sealed interface HomeEvent {
    data class NavigateToDetail(val id: Int): HomeEvent
}

// ViewModel orchestration
class HomeViewModel : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    val events = Channel<HomeEvent>()

    fun onAction(action: HomeAction) { /* ... */ }
}
```

### Offline-First Approach

The app prioritizes local data and gracefully handles network unavailability:

1. **Read from database first** - Instant UI updates
2. **Fetch from API in background** - Keep data fresh
3. **Cache API responses** - Reduce network calls
4. **Smart search** - Local search with API fallback

```kotlin
override suspend fun searchPokemon(query: String): Result<List<Pokemon>, DataError.Network> {
    // 1. Search local database first
    val localResults = pokemonDao.searchPokemonByName("%$query%")
    if (localResults.isNotEmpty()) {
        return Result.Success(localResults.map { it.toDomain() })
    }

    // 2. Fallback to API if not found locally
    val apiResult = safeCall { apiService.searchPokemon(query) }

    // 3. Cache API result for future use
    if (apiResult is Result.Success) {
        pokemonDao.insertPokemon(apiResult.data.toEntity())
    }

    return apiResult
}
```

### Type-Safe Error Handling

Custom `Result` type eliminates try-catch boilerplate and enforces error handling:

```kotlin
sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E>(val error: E): Result<Nothing, E>
}

sealed interface DataError {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
}
```

### Dependency Injection with Koin

Modular DI setup with clear separation of concerns:

```kotlin
val appModules = listOf(
    databaseModule,      // Room database
    networkModule,       // Retrofit + OkHttp
    authModule,          // Authentication
    homeDataModule,      // Pokemon data layer
    homeViewModelModule  // Pokemon presentation layer
)
```

## API Reference

This app consumes the [PokéAPI](https://pokeapi.co/) for Pokemon data.

**Base URL:** `https://pokeapi.co/api/v2/`

**Endpoints used:**
- `GET /pokemon?limit={limit}&offset={offset}` - List Pokemon
- `GET /pokemon/{id or name}` - Get Pokemon details

## Setup & Installation

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 17 or higher
- Android SDK 34 (API 34)
- Minimum Android SDK 24 (API 24)

### Running the Project

1. Clone the repository
```bash
git clone <repository-url>
cd Pokemon
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device

### Build Variants
- **Debug** - Development build with logging enabled
- **Release** - Production build with ProGuard/R8 optimization

## Testing

The architecture supports comprehensive testing:

- **Unit Tests** - ViewModels, UseCases, Mappers
- **Integration Tests** - Repository, Database
- **UI Tests** - Compose UI testing

## Code Quality

The project maintains code quality through:

- **Kotlin coding conventions** - Official style guide compliance
- **Explicit types** - Enhanced readability and compile-time safety
- **Immutability** - `val` over `var`, data classes for states
- **Single responsibility** - Each class has one clear purpose
- **Null safety** - Leverage Kotlin's type system

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Follow the existing architecture and patterns
2. Write meaningful commit messages
3. Add tests for new features
4. Update documentation as needed

## License

This project is for educational purposes and portfolio demonstration.

---

**Built with ❤️ using modern Android development practices**
