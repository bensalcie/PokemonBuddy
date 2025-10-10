package bensalcie.app.domain.repository

import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.model.PokemonDetails


interface PokemonRepository {
    suspend fun getPokemonList(): ApiResult<List<Pokemon>>
    suspend fun getPokemonDetails(name: String): ApiResult<PokemonDetails>
}
