package bensalcie.app.pokmonbuddy.details.ui

import bensalcie.app.domain.model.PokemonDetails


sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(val details: PokemonDetails) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}
