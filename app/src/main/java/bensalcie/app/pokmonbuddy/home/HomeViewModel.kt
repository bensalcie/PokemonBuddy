package bensalcie.app.pokmonbuddy.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.repository.PokemonRepository
import bensalcie.app.pokmonbuddy.home.ui.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState

    private var offset = 0
    private val limit = 100

    init {
        loadPokemons()
    }

    private var isLoadingMore = false

    fun loadPokemons(isLoadMore: Boolean = false) {
        if (isLoadMore && isLoadingMore) return
        isLoadingMore = true

        viewModelScope.launch {
            repository.getPokemonList(offset, limit).collectLatest { result ->
                when (result) {
                    is ApiResult.Loading -> if (!isLoadMore) _uiState.update { HomeUiState.Loading }
                    is ApiResult.Success -> {
                        _uiState.update {
                            val newList = if (isLoadMore && it is HomeUiState.Success) {
                                it.pokeMons + result.data
                            } else result.data
                            HomeUiState.Success(newList)
                        }
                        offset += limit
                    }
                    is ApiResult.Error -> _uiState.update {
                        HomeUiState.Error(result.throwable.message ?: "Failed to load Pokémon")
                    }
                }
            }
            isLoadingMore = false
        }
    }


    fun reloadData() {
        offset = 0
        loadPokemons()
    }
}
