package bensalcie.app.pokmonbuddy.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.sp
import bensalcie.app.core.network.ApiResult
import bensalcie.app.pokmonbuddy.util.NetworkMonitor
import bensalcie.app.domain.model.Pokemon
import bensalcie.app.pokmonbuddy.R
import bensalcie.app.pokmonbuddy.components.NoInternetView
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.get

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onPokemonClick: (String) -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }

    val context = LocalContext.current.applicationContext
    val networkMonitor: NetworkMonitor = getKoin().get() // Inject networkMonitor by koinInject<NetworkMonitor>()
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
                SearchBar(
                    query = query,
                    onQueryChange = {
                        query = it
                    }
                )
                Spacer(Modifier.height(16.dp))

                when (uiState) {
                    ApiResult.Loading ->  Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    is ApiResult.Error -> ErrorState(
                        (uiState as ApiResult.Error).throwable.message ?: "Error"
                    )

                    is ApiResult.Success -> {
                        val data = (uiState as ApiResult.Success<List<Pokemon>>).data
                        val filtered = data.filter { it.name.contains(query, ignoreCase = true) }
                        PokemonGrid(pokemonList = filtered, onClick = onPokemonClick)
                    }
                }
            }
        }
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
        placeholder = { Text("Search by Name or number") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        textStyle = TextStyle(color =MaterialTheme.colorScheme.onBackground ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor =Color.White.copy(alpha = 0.5f),
            focusedContainerColor = Color.White.copy(alpha = 0.5f),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun PokemonGrid(pokemonList: List<Pokemon>, onClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(pokemonList) { pokemon ->
            PokemonCard(pokemon = pokemon, onClick = { onClick(pokemon.name) })
        }
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    val pastelColors = listOf(
        Color(0xFFD6F0E6),
        Color(0xFFDDE6F5),
        Color(0xFFFCE8D7),
        Color(0xFFF5D6D6)
    )
    val bg = remember(pokemon.name.hashCode()) {
        pastelColors.random()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1C1C1E)
            )
            Text(
                text = pokemon.imageUrl?.substringAfterLast("/")?.substringBefore(".png")
                    ?.padStart(3, '0') ?: "",
                fontSize = 14.sp,
                color = Color(0xFF7A7A7A)
            )
        }
    }
}

@Composable
fun LoadingGrid() {
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(6) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
            )
        }
    })
}

@Composable
fun ErrorState(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}
