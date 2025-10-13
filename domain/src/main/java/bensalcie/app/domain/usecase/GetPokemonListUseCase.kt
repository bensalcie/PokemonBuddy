package bensalcie.app.domain.usecase

import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow


class GetPokemonListUseCase(private val repo: PokemonRepository) {

    operator fun invoke(
        offset: Int = 0,
        limit: Int = 100
    ): Flow<ApiResult<List<Pokemon>>> {
        return repo.getPokemonList(offset, limit)
    }
}