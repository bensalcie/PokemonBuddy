package bensalcie.app.pokmonbuddy.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val getList: GetPokemonListUseCase) : ViewModel() {
    private val _state = MutableStateFlow<ApiResult<List<Pokemon>>>(ApiResult.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = ApiResult.Loading
            _state.value = getList()
        }
    }
}
