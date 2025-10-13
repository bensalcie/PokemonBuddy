package bensalcie.app.domain.repository

import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.model.PokemonDetails
import kotlinx.coroutines.flow.Flow


interface PokemonRepository {
    fun getPokemonList(offset: Int, limit: Int): Flow<ApiResult<List<Pokemon>>>
    fun getPokemonDetails(name: String): Flow<ApiResult<PokemonDetails>>
}
