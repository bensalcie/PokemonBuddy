package bensalcie.app.pokmonbuddy.home

import android.util.Log
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
    private var isLoadingMore = false

    init {
        loadPokemons()
    }

    fun loadPokemons(isLoadMore: Boolean = false) {
        if (isLoadMore && isLoadingMore) return

        viewModelScope.launch {
            if (isLoadMore) {
                Log.d("HomeVM", "🔄 Load more triggered!")
                isLoadingMore = true

                // ✅  reflect "loading more" state
                _uiState.update {
                    if (it is HomeUiState.Success) {
                        Log.d("HomeVM", "🟢 Updating UI with isLoadingMore = true")
                        it.copy(isLoadingMore = true)
                    } else {
                        it
                    }
                }
            } else {
                _uiState.value = HomeUiState.Loading
            }

            repository.getPokemonList(offset, limit).collectLatest { result ->
                when (result) {
                    is ApiResult.Success -> {

                        _uiState.update { current ->
                            val newList = if (isLoadMore && current is HomeUiState.Success) {
                                current.pokeMons + result.data
                            } else result.data
                            HomeUiState.Success(newList, isLoadingMore = false)
                        }
                        offset += limit
                        isLoadingMore = false
                    }

                    is ApiResult.Error -> {
                        _uiState.value = HomeUiState.Error(
                            result.throwable.message ?: "Failed to load Pokémon"
                        )
                        isLoadingMore = false
                    }

                    is ApiResult.Loading -> {
                    }
                }
            }
        }
    }

    fun reloadData() {
        offset = 0
        loadPokemons()
    }
}

