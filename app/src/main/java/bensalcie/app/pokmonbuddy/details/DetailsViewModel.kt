package bensalcie.app.pokmonbuddy.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.repository.PokemonRepository
import bensalcie.app.pokmonbuddy.details.ui.DetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    fun loadDetails(name: String) {
        viewModelScope.launch {
            repository.getPokemonDetails(name).collectLatest { result ->
                when (result) {
                    is ApiResult.Loading -> _uiState.update { DetailsUiState.Loading }

                    is ApiResult.Success -> _uiState.update {
                        DetailsUiState.Success(result.data)
                    }

                    is ApiResult.Error -> _uiState.update {
                        DetailsUiState.Error(
                            result.throwable.message ?: "Failed to load Pokémon details"
                        )
                    }
                }
            }
        }
    }

    fun retry(name: String) {
        loadDetails(name)
    }
}
