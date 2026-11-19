package com.movieshelf.app.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.data.model.MovieStatus
import com.movieshelf.app.ui.components.BrandLabel
import com.movieshelf.app.ui.components.MsGhostButton
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
import com.movieshelf.app.ui.theme.MsTealSoft
import com.movieshelf.app.ui.theme.Outfit
import com.movieshelf.app.ui.theme.SpaceGrotesk

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val movie by viewModel.movie.collectAsState()

    if (movie == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MsBg),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = MsAmber)
        }
        return
    }

    val m = movie!!

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MsBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MsTextButton(text = "← Wróć", onClick = onBack)
            MsTextButton(text = "Edytuj", onClick = { onEdit(m.id) }, accent = true)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            ShelfPoster(movie = m, width = 110.dp, height = 154.dp, corner = 14.dp)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                BrandLabel()
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = m.title,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 26.sp,
                    color = MsInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = listOfNotNull(m.year, m.genre).joinToString(" · "),
                    fontFamily = Outfit,
                    fontSize = 13.sp,
                    color = MsInkSoft
                )
                Spacer(modifier = Modifier.height(8.dp))
                val filled = m.rating.toInt().coerceIn(0, 5)
                Text(
                    text = "★".repeat(filled) + "☆".repeat(5 - filled) +
                        if (m.rating > 0) "  ${"%.1f".format(m.rating)}" else "",
                    color = MsAmber,
                    fontFamily = Outfit,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        InfoPanel(title = "Opis", body = m.overview.ifBlank { "Brak opisu." })
        Spacer(modifier = Modifier.height(10.dp))
        InfoPanel(
            title = "Notatka",
            body = m.note ?: "Brak własnej notatki — dodaj w edycji.",
            italic = m.note == null
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (!m.locationLabel.isNullOrBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MsTealSoft, RoundedCornerShape(18.dp))
                    .border(1.dp, MsTeal.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Miejsce seansu",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MsInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = m.locationLabel,
                    fontFamily = Outfit,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MsInk
                )
                if (m.locationLat != null && m.locationLng != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "%.3f °N, %.3f °E · zapisane lokalnie".format(m.locationLat, m.locationLng),
                        fontFamily = Outfit,
                        fontSize = 12.sp,
                        color = MsTeal
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        MsPrimaryButton(
            text = if (m.status == MovieStatus.WATCHED) "Obejrzany ✓" else "Oznacz jako obejrzany",
            onClick = viewModel::markWatched
        )
        Spacer(modifier = Modifier.height(8.dp))
        MsGhostButton(
            text = if (m.isFavorite) "♥ Ulubione" else "♡ Dodaj do ulubionych",
            onClick = viewModel::toggleFavorite
        )
    }
}

@Composable
private fun InfoPanel(title: String, body: String, italic: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MsBgElevated, RoundedCornerShape(18.dp))
            .border(1.dp, MsLine, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Text(
            text = title,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MsInk
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = body,
            fontFamily = Outfit,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MsInkSoft,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal
        )
    }
}
