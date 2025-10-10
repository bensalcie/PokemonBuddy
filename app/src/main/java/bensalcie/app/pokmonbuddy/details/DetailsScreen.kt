package bensalcie.app.pokmonbuddy.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.PokemonDetails
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailsScreen(name: String, onBack: () -> Unit) {
    val vm: DetailsViewModel = koinViewModel(parameters = { parametersOf(name) })
    val state by vm.state.collectAsState()

    when (state) {
        ApiResult.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is ApiResult.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text((state as ApiResult.Error).throwable.message ?: "Error")
        }

        is ApiResult.Success -> Content((state as ApiResult.Success<PokemonDetails>).data, onBack)
    }
}

@Composable
fun Content(details: PokemonDetails, onBack: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Button(onClick = onBack) { Text("Back") }
        Spacer(Modifier.height(16.dp))
        AsyncImage(
            model = details.imageUrl,
            contentDescription = details.name,
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            details.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(12.dp))
        Text("Stats:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        details.stats.forEach {
            Text("${it.name}: ${it.value}")
        }
    }
}
