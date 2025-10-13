package bensalcie.app.pokmonbuddy.home.ui

import bensalcie.app.domain.model.Pokemon

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object Loading : HomeUiState
    data class Success(val pokeMons: List<Pokemon>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
