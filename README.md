# 🚀 Project Setup

## App Screenshots


# Pokémon Buddy

## 📱 Screenshots

<table>
  <tr>
    <td>
      <img width="300" height="600" alt="Screenshot 1" src="https://github.com/bensalcie/PokemonBuddy/blob/main/screenshots/Screenshot%202025-10-13%20at%2013.52.27.png" />
    </td>
    <td>
      <img width="300" height="600" alt="Screenshot 2" src="https://github.com/bensalcie/PokemonBuddy/blob/main/screenshots/Screenshot%202025-10-13%20at%2013.52.37.png" />
    </td>
  </tr>
  <tr>
    <td>
      <img width="300" height="600" alt="Screenshot 3" src="https://github.com/bensalcie/PokemonBuddy/blob/main/screenshots/Screenshot%202025-10-13%20at%2013.53.31.png" />
    </td>
    <td>
      <img width="300" height="600" alt="Screenshot 4" src="https://github.com/bensalcie/PokemonBuddy/blob/main/screenshots/Screenshot%202025-10-13%20at%2013.53.36.png" />
    </td>
  </tr>
</table>


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
