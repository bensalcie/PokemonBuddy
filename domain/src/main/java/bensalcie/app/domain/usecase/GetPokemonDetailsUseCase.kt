package bensalcie.app.domain.usecase

import bensalcie.app.domain.repository.PokemonRepository


class GetPokemonDetailsUseCase(private val repo: PokemonRepository) {
    operator fun invoke(name: String) = repo.getPokemonDetails(name)
}
