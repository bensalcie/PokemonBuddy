package bensalcie.app.pokmonbuddy.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bensalcie.app.pokmonbuddy.R
import bensalcie.app.pokmonbuddy.components.NoInternetView
import bensalcie.app.pokmonbuddy.home.ui.ErrorState
import bensalcie.app.pokmonbuddy.home.ui.HomeUiState
import bensalcie.app.pokmonbuddy.home.ui.PokemonGrid
import bensalcie.app.pokmonbuddy.home.ui.SearchBar
import bensalcie.app.pokmonbuddy.util.NetworkMonitor
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onPokemonClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current.applicationContext
    val networkMonitor: NetworkMonitor = getKoin().get()
    val scope = rememberCoroutineScope()
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
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                Text(
                    text = stringResource(R.string.app_name_title),
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
                    is HomeUiState.Loading ->

                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                        }

                    is HomeUiState.Error -> ErrorState((uiState as HomeUiState.Error).message)

                    is HomeUiState.Success -> {
                        val successState = uiState as HomeUiState.Success
                        val data = successState.pokeMons
                        val filtered = data.filter { it.name.contains(query, ignoreCase = true) }

                        PokemonGrid(
                            pokemonList = filtered,
                            onClick = onPokemonClick,
                            onLoadMore = { viewModel.loadPokemons(isLoadMore = true) },
                            isLoadingMore = successState.isLoadingMore
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
