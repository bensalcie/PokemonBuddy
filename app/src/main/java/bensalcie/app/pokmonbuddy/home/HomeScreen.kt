package bensalcie.app.pokmonbuddy.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.Pokemon
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onPokemonClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is ApiResult.Loading -> LoadingList()
        is ApiResult.Error -> ErrorState(
            (state as ApiResult.Error).throwable.message ?: "Unknown error"
        )

        is ApiResult.Success -> SuccessList(
            (state as ApiResult.Success<List<Pokemon>>).data,
            onPokemonClick
        )
    }
}

@Composable
fun LoadingList() {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(10) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = 4.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}

@Composable
fun SuccessList(data: List<Pokemon>, onPokemonClick: (String) -> Unit) {
    LazyColumn {
        items(data.size) { index ->
            val pokemon = data[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onPokemonClick(pokemon.name) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
