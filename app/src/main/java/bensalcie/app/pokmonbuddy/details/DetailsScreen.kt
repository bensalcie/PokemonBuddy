package bensalcie.app.pokmonbuddy.details


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bensalcie.app.domain.model.PokemonDetails
import bensalcie.app.pokmonbuddy.details.ui.DetailsUiState
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import java.util.*

@Composable
fun DetailsScreen(name: String, onBack: () -> Unit) {
    val viewModel: DetailsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Fetch data on first composition
    LaunchedEffect(name) {
        viewModel.loadDetails(name)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            is DetailsUiState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is DetailsUiState.Error -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (uiState as DetailsUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is DetailsUiState.Success -> {
                val details = (uiState as DetailsUiState.Success).details
                DetailsContent(details, onBack)
            }
        }
    }
}

@Composable
private fun DetailsContent(details: PokemonDetails, onBack: () -> Unit) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(id = bensalcie.app.pokmonbuddy.R.string.tab_stats),
        stringResource(id = bensalcie.app.pokmonbuddy.R.string.tab_moves),
        stringResource(id = bensalcie.app.pokmonbuddy.R.string.tab_details),
        stringResource(id = bensalcie.app.pokmonbuddy.R.string.tab_types),
        stringResource(id = bensalcie.app.pokmonbuddy.R.string.tab_dimensions)
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top bar
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(bensalcie.app.pokmonbuddy.R.string.cd_back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.weight(0.05f))
            Text(
                text = details.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(10.dp))

        // Pokémon image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = details.imageUrl,
                contentDescription = details.name,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Scrollable Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 0.dp,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index)
                                MaterialTheme.colorScheme.onBackground
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        when (selectedTab) {
            0 -> StatsTab(details)
            1 -> MovesTab(details)
            2 -> DetailTab(details)
            3 -> TypesTab(details)
            4 -> MeasurementTab(details)
        }

        Spacer(Modifier.height(60.dp))
    }
}

/** Tabs */

@Composable
private fun MovesTab(details: PokemonDetails) {
    Column {
        // Show dynamic images instead of repeating 3 hardcoded ones
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            details.moves.take(3).forEach { move ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = details.imageUrl,
                        contentDescription = move.name,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        details.moves.forEachIndexed { index, move ->
            Text(
                text = "(${index + 1}) ${move.name.replaceFirstChar { it.uppercase() }}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun DetailTab(details: PokemonDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(
                bensalcie.app.pokmonbuddy.R.string.label_name,
                details.name.replaceFirstChar { it.uppercase() }),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(
                bensalcie.app.pokmonbuddy.R.string.label_species,
                details.speciesName.replaceFirstChar { it.uppercase() }),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun TypesTab(details: PokemonDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        details.types.forEachIndexed { index, type ->
            Text(
                text = stringResource(
                    bensalcie.app.pokmonbuddy.R.string.label_type_item,
                    index + 1,
                    type.name.replaceFirstChar { it.uppercase() }),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun StatsTab(details: PokemonDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        details.stats.forEachIndexed { index, stat ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        bensalcie.app.pokmonbuddy.R.string.label_stat_item,
                        index + 1,
                        stat.name.replaceFirstChar { it.uppercase() }),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${stat.value}",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun MeasurementTab(details: PokemonDetails) {
    Column {
        Text(
            text = stringResource(
                bensalcie.app.pokmonbuddy.R.string.label_measurements,
                details.weight,
                details.height
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
