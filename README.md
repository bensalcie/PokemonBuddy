### 🚀 Project Setup

# Minimum SDK: 24
# Language: Kotlin
# UI Framework: Jetpack Compose
# Architecture: MVVM + Clean
# Dependency Injection: Koin
# Modules:
 ```
:app – UI and DI setup
 :data – API, repository implementations
 :domain – use cases, models
 :core – shared utilities (network, error handling, constants)
```

```
core/
    ├── network/
    │   ├── ApiResult.kt
    │   └── NetworkModule.kt
  data/
    ├── api/
    │   └── PokeApiService.kt
    ├── repository/
    │   └── PokemonRepositoryImpl.kt
  domain/
    ├── model/
    │   └── Pokemon.kt
    ├── repository/
    │   └── PokemonRepository.kt
    └── usecase/
        └── GetPokemonListUseCase.kt
  app/
    ├── ui/
    │   ├── home/
    │   ├── details/
    │   └── theme/
    └── di/AppModule.kt
```
