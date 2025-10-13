package bensalcie.app.pokmonbuddy.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bensalcie.app.pokmonbuddy.R
import bensalcie.app.pokmonbuddy.components.NoInternetView
import bensalcie.app.pokmonbuddy.home.ui.HomeUiState
import bensalcie.app.pokmonbuddy.home.ui.PokemonGrid
import bensalcie.app.pokmonbuddy.util.NetworkMonitor
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@Composable
fun ErrorState(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        placeholder = { Text("Search by Name") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
            focusedContainerColor = Color.White.copy(alpha = 0.5f),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onPokemonClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }

    val context = LocalContext.current.applicationContext
    val networkMonitor: NetworkMonitor = getKoin().get()
    val scope = rememberCoroutineScope()
    val backgroundColor = MaterialTheme.colorScheme.background

    var isConnected by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        networkMonitor.observe(context).collect { status ->
            isConnected = status
        }
    }

    if (!isConnected) {
        NoInternetView(onRetry = {
            scope.launch {
                isConnected = networkMonitor.isNetworkAvailable(context)
                if (isConnected) viewModel.reloadData()
            }
        })
    } else {
        Scaffold(
            containerColor = backgroundColor,
            topBar = {
                Text(
                    text = stringResource(id = R.string.app_name_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 14.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                SearchBar(query = query, onQueryChange = { query = it })
                Spacer(Modifier.height(16.dp))

                when (uiState) {
                    is HomeUiState.Loading -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    is HomeUiState.Error -> ErrorState((uiState as HomeUiState.Error).message)

                    is HomeUiState.Success -> {
                        val data = (uiState as HomeUiState.Success).pokeMons
                        val filtered = data.filter { it.name.contains(query, ignoreCase = true) }
                        PokemonGrid(
                            pokemonList = filtered,
                            onClick = onPokemonClick,
                            onLoadMore = { viewModel.loadPokemons(isLoadMore = true) }
                        )
                    }

                    HomeUiState.Idle -> {
                        LaunchedEffect(Unit) { viewModel.loadPokemons() }
                    }
                }
            }
        }
    }
}
