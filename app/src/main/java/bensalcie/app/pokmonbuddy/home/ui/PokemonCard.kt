package bensalcie.app.pokmonbuddy.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bensalcie.app.domain.model.Pokemon
import coil.compose.AsyncImage

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
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(60.dp)
                    .padding(bottom = 3.dp)
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