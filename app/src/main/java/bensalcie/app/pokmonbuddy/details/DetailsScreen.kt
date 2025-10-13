package bensalcie.app.pokmonbuddy.details

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.PokemonDetails
import bensalcie.app.pokmonbuddy.details.ui.DetailsUiState
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

import org.koin.core.parameter.parametersOf
import java.util.Locale

@Composable
fun DetailsScreen(name: String, onBack: () -> Unit) {
    val vm: DetailsViewModel = koinViewModel(parameters = { parametersOf(name) })
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    // Trigger fetching when screen is first composed
    LaunchedEffect(name) {
        vm.loadDetails(name)
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background

    ) {
        when (uiState) {
            is DetailsUiState.Loading -> Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is DetailsUiState.Error -> Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text((uiState as DetailsUiState.Error).message)
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
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Stats", "Moves", "Detail", "Types", "Dimensions")

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top bar
        Row(
            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.weight(0.05f))
            Text(
                text = details.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(10.dp))

        // Image box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.onBackground),
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
            contentColor = MaterialTheme.colorScheme.onBackground,
            edgePadding = 0.dp,
            divider = {}) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = {
                    Text(
                        text = title,
                        color = if (selectedTab == index) MaterialTheme.colorScheme.onBackground else Color.Gray,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                })
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

        Spacer(Modifier.height(60.dp)) // bottom padding
    }
}

/** Tabs Below the Image
 *
 */

@Composable
private fun MovesTab(details: PokemonDetails) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = details.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        details.moves.forEachIndexed { index, move ->
            Text(
                text = "(${index + 1}) ${move.name.capitalize(Locale.ROOT)}\n",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.height(6.dp))
    }
}

@Composable
private fun DetailTab(details: PokemonDetails) {
    Column {
        Text(
            text = " (1) Name: ${details.name.replaceFirstChar { it.uppercase() }}",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = " (2) Species: ${details.speciesName.capitalize(Locale.ROOT)}",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun TypesTab(details: PokemonDetails) {

    Column {
        details.types.forEachIndexed { index, type ->

            Text(
                " (${index + 1}) ${type.name.capitalize(Locale.ROOT)}",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun StatsTab(details: PokemonDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        details.stats.forEachIndexed { index, it ->
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    " (${index + 1}) ${it.name.replaceFirstChar { c -> c.uppercase() }}",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "${it.value}", color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun MeasurementTab(details: PokemonDetails) {
    Column {
        Text(
            " (1) Weight: ${details.weight} HG\n (2) Height: ${details.height} DM",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
