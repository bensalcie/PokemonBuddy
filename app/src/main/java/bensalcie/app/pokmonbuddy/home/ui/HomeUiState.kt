package bensalcie.app.pokmonbuddy.home.ui

import bensalcie.app.domain.model.Pokemon

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object Loading : HomeUiState
    data class Success(
        val pokeMons: List<Pokemon>,
        val isLoadingMore: Boolean = false
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}
