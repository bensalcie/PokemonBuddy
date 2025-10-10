package bensalcie.app.data.repository

import bensalcie.app.core.network.ApiResult
import bensalcie.app.data.api.PokeApiService
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.model.PokemonDetails
import bensalcie.app.domain.model.Stat
import bensalcie.app.domain.repository.PokemonRepository


class PokemonRepositoryImpl(private val api: PokeApiService) : PokemonRepository {

    override suspend fun getPokemonList(): ApiResult<List<Pokemon>> {
        return try {
            ApiResult.Loading
            val result = api.getPokemonList().results.map { entry ->
                val id = entry.url.trimEnd('/').split("/").last()
                val image =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
                Pokemon(entry.name, image)
            }
            ApiResult.Success(result)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun getPokemonDetails(name: String): ApiResult<PokemonDetails> {
        return try {
            ApiResult.Loading
            val details = api.getPokemonDetails(name)
            val stats = details.stats?.map {
                Stat(it.stat.name, it.base_stat)
            } ?: emptyList()
            ApiResult.Success(PokemonDetails(details.name, details.sprites?.front_default, stats))
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
