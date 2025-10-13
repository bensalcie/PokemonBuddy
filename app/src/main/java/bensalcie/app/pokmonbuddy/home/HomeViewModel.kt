package bensalcie.app.pokmonbuddy.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.usecase.GetPokemonListUseCase
import bensalcie.app.pokmonbuddy.home.ui.HomeUiState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPokemonsUseCase: GetPokemonListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadPokeMons() {
        viewModelScope.launch {
            _uiState.update { HomeUiState.Loading }

            when (val result = getPokemonsUseCase()) {
                is ApiResult.Success -> _uiState.update {
                    HomeUiState.Success(result.data)
                }

                is ApiResult.Error -> _uiState.update {
                    HomeUiState.Error(result.throwable.message ?: "Unknown error")
                }

                ApiResult.Loading -> _uiState.update { HomeUiState.Loading }
            }
        }
    }

    fun reloadData() {
        loadPokeMons()
    }
}
