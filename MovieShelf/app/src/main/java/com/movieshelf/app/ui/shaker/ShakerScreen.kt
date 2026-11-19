package com.movieshelf.app.ui.shaker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.ui.components.BottomTab
import com.movieshelf.app.ui.components.BrandLabel
import com.movieshelf.app.ui.components.MovieShelfBottomBar
import com.movieshelf.app.ui.components.MsPrimaryButton
import com.movieshelf.app.ui.components.MsTextButton
import com.movieshelf.app.ui.components.ShelfPoster
import com.movieshelf.app.ui.theme.MsAmber
import com.movieshelf.app.ui.theme.MsBg
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.MsTeal
import com.movieshelf.app.ui.theme.Outfit
import com.movieshelf.app.ui.theme.SpaceGrotesk
import kotlinx.coroutines.launch

/** Movie Shaker — losuje z lokalnej watchlisty (Room), działa offline */
@Composable
fun ShakerScreen(
    viewModel: ShakerViewModel,
    onBack: () -> Unit,
    onOpenShelf: () -> Unit,
    onOpenSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val picked by viewModel.picked.collectAsState()
    val empty by viewModel.emptyWatchlist.collectAsState()
    val shakeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val pulse = rememberInfiniteTransition(label = "orbPulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "pulseScale"
    )

    fun runShake() {
        scope.launch {
            repeat(4) { i ->
                shakeOffset.animateTo(if (i % 2 == 0) -10f else 10f, tween(60))
            }
            shakeOffset.animateTo(0f, tween(60))
            viewModel.shake()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(MsAmber.copy(alpha = 0.16f), MsBg),
                    radius = 700f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 78.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                MsTextButton(text = "← Wróć", onClick = onBack)
            }

            Spacer(modifier = Modifier.height(12.dp))
            BrandLabel()
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Movie Shaker",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = MsInk
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Potrząśnij telefonem — wylosujemy film z listy „Do obejrzenia”.",
                fontFamily = Outfit,
                fontSize = 14.sp,
                color = MsInkSoft,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer {
                        translationX = shakeOffset.value
                        rotationZ = shakeOffset.value * 0.6f
                    }
                    .scale(pulseScale)
                    .shadow(16.dp, CircleShape, ambientColor = MsAmber.copy(0.2f), spotColor = MsAmber.copy(0.2f))
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Color.White, Color(0xFFFFE8C2))))
                    .border(1.dp, Color(0xFFF0D4A8), CircleShape)
                    .clickable { runShake() },
                contentAlignment = Alignment.Center
            ) {
                Text("⟳", fontSize = 40.sp, color = MsAmber)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "lub tapnij orb, żeby zasymulować shake",
                fontFamily = Outfit,
                fontSize = 12.sp,
                color = MsInkSoft
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (empty) {
                Text(
                    text = "Brak filmów „Do obejrzenia”. Dodaj coś na półkę.",
                    fontFamily = Outfit,
                    color = MsInkSoft,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            picked?.let { movie ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MsBgElevated, RoundedCornerShape(18.dp))
                        .border(1.dp, MsLine, RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShelfPoster(movie = movie, width = 44.dp, height = 62.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "NA DZIŚ WIECZÓR",
                            fontFamily = Outfit,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = MsTeal
                        )
                        Text(
                            text = movie.title,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = MsInk
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            MsPrimaryButton(
                text = "Wylosuj teraz",
                onClick = { runShake() },
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }

        MovieShelfBottomBar(
            active = BottomTab.SHAKER,
            onShelf = onOpenShelf,
            onShaker = {},
            onAdd = onOpenSearch,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
