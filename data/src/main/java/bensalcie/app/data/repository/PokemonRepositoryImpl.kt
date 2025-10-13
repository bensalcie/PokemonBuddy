package bensalcie.app.data.repository

import bensalcie.app.core.network.ApiResult
import bensalcie.app.core.util.Constants.IMAGE_BASE_URL
import bensalcie.app.data.api.PokeApiService
import bensalcie.app.data.utils.PokemonMappers
import bensalcie.app.domain.model.Move
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.model.PokemonDetails
import bensalcie.app.domain.model.Stat
import bensalcie.app.domain.model.Type
import bensalcie.app.domain.repository.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class PokemonRepositoryImpl(
    private val api: PokeApiService,
    private val ioDispatcher: CoroutineDispatcher
) : PokemonRepository {

    override fun getPokemonList(
        offset: Int,
        limit: Int
    ): Flow<ApiResult<List<Pokemon>>> = flow {
        emit(ApiResult.Loading)

        val result = withContext(ioDispatcher) {
            try {
                val response = api.getPokemonList(offset, limit)
                val mapped = response.results.map { PokemonMappers.mapToDomain(it) }
                ApiResult.Success(mapped)
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }

        emit(result)
    }.flowOn(ioDispatcher)

    override fun getPokemonDetails(name: String): Flow<ApiResult<PokemonDetails>> = flow {
        emit(ApiResult.Loading)

        val result = withContext(ioDispatcher) {
            try {
                val response = api.getPokemonDetails(name)
                ApiResult.Success(PokemonMappers.mapDetailsToDomain(response))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }

        emit(result)
    }.flowOn(ioDispatcher)
}