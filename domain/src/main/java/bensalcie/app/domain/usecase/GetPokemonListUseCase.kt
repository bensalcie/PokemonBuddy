package bensalcie.app.domain.usecase

import bensalcie.app.domain.repository.PokemonRepository


class GetPokemonListUseCase(private val repo: PokemonRepository) {
    suspend operator fun invoke() = repo.getPokemonList()
}
