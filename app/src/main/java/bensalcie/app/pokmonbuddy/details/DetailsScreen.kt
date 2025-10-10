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
import bensalcie.app.core.network.ApiResult
import bensalcie.app.domain.model.PokemonDetails
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

import org.koin.core.parameter.parametersOf

@Composable
fun DetailsScreen(name: String, onBack: () -> Unit) {
    val vm: DetailsViewModel = koinViewModel(parameters = { parametersOf(name) })
    val uiState by vm.state.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color =  MaterialTheme.colorScheme.background

    ) {
        when (uiState) {
            ApiResult.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            is ApiResult.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text((uiState as ApiResult.Error).throwable.message ?: "Error")
            }

            is ApiResult.Success -> {
                val details = (uiState as ApiResult.Success<PokemonDetails>).data
                DetailsContent(details, onBack)
            }
        }
    }
}

@Composable
private fun DetailsContent(details: PokemonDetails, onBack: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Stats", "Forms", "Detail", "Types", "Weaknesses")

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
                    contentDescription = "Back",
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

        Spacer(Modifier.height(4.dp))

        Text(
            text = "#${
                details.imageUrl?.substringAfterLast('/')?.substringBefore(".png")
                    ?.padStart(3, '0') ?: ""
            }",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color =  MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(12.dp))

        // Image box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background( MaterialTheme.colorScheme.onBackground),
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
            contentColor =  MaterialTheme.colorScheme.onBackground,
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
                            color = if (selectedTab == index) MaterialTheme.colorScheme.onBackground else Color.Gray,
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
            1 -> FormsTab(details)
            2 -> DetailTab(details)
            3 -> TypesTab(details)
            4 -> WeaknessTab()
        }

        Spacer(Modifier.height(60.dp)) // bottom padding
    }
}

/** Tabs Below the Image
 *
 */

@Composable
private fun FormsTab(details: PokemonDetails) {
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
                        .background( MaterialTheme.colorScheme.onBackground),
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
        Text(
            text = "Mega Evolution",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "In order to support its flower, which has grown larger due to Mega Evolution, its back and legs have become stronger.",
            color =  MaterialTheme.colorScheme.onBackground,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun DetailTab(details: PokemonDetails) {
    Column {
        Text(
            text = "Name: ${details.name.replaceFirstChar { it.uppercase() }}",
            fontWeight = FontWeight.Medium,
            color =  MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(6.dp))
        Text(text = "More info will be added here.", color =  MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun TypesTab(details: PokemonDetails) {
    Column {
        Text("Types section coming soon", color =  MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun StatsTab(details: PokemonDetails) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        details.stats.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    it.name.replaceFirstChar { c -> c.uppercase() },
                    color =  MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "${it.value}",
                    color =  MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun WeaknessTab() {
    Column {
        Text("Weakness info will be displayed here.", color =  MaterialTheme.colorScheme.onBackground)
    }
}
