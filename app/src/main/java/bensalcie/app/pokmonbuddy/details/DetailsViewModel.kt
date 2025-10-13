package bensalcie.app.pokmonbuddy.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.usecase.GetPokemonDetailsUseCase
import bensalcie.app.pokmonbuddy.details.ui.DetailsUiState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
    private val pokemonName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            when (val result = getPokemonDetailsUseCase(pokemonName)) {
                is ApiResult.Success -> _uiState.update {
                    DetailsUiState.Success(result.data)
                }

                is ApiResult.Error -> _uiState.update {
                    DetailsUiState.Error(result.throwable.message ?: "Failed to load details")
                }

                ApiResult.Loading -> _uiState.update { DetailsUiState.Loading }
            }
        }
    }
}
