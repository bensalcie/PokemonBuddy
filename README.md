### 🚀 Project Setup

## App Screenshots

<img width="300" height="600" alt="image" src="https://github.com/user-attachments/assets/d206163e-b524-4fce-9139-e1c34c458363" />
<img width="300" height="600" alt="image" src="https://github.com/user-attachments/assets/17a20e77-9abe-4003-a29e-731558792bb7" />

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
