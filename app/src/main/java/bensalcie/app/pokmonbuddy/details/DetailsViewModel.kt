package bensalcie.app.pokmonbuddy.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.PokemonDetails
import bensalcie.app.domain.usecase.GetPokemonDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val getDetails: GetPokemonDetailsUseCase, private val name: String) :
    ViewModel() {
    private val _state = MutableStateFlow<ApiResult<PokemonDetails>>(ApiResult.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = ApiResult.Loading
            _state.value = getDetails(name)
        }
    }
}
