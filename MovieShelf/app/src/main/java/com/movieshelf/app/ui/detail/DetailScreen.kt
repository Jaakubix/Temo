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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.data.demo.demoMovieById
import com.movieshelf.app.ui.components.BrandLabel
import com.movieshelf.app.ui.components.MsGhostButton
import com.movieshelf.app.ui.components.MsPrimaryButton
import com.movieshelf.app.ui.components.MsTextButton
import com.movieshelf.app.ui.components.PosterPlaceholder
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

/** Ekran szczegółów — układ jak w makiecie (hero, panele, GPS, akcje) */
@Composable
fun DetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = demoMovieById(movieId)
    val runtime = movie.runtimeMinutes?.let { " · $it min" }.orEmpty()

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
            MsTextButton(text = "Edytuj", onClick = { onEdit(movie.id) }, accent = true)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            PosterPlaceholder(
                styleIndex = movie.posterStyle,
                width = 110.dp,
                height = 154.dp,
                corner = 14.dp
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                BrandLabel()
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.title,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 26.sp,
                    color = MsInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${movie.year} · ${movie.genre}$runtime",
                    fontFamily = Outfit,
                    fontSize = 13.sp,
                    color = MsInkSoft
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Ocena gwiazdkowa jak w makiecie
                val filled = movie.rating.toInt().coerceIn(0, 5)
                Text(
                    text = "★".repeat(filled) + "☆".repeat(5 - filled) + "  ${"%.1f".format(movie.rating)}",
                    color = MsAmber,
                    fontFamily = Outfit,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        InfoPanel(title = "Opis", body = movie.overview)
        Spacer(modifier = Modifier.height(10.dp))
        InfoPanel(
            title = "Notatka",
            body = movie.note ?: "Brak własnej notatki — dodaj w edycji.",
            italic = movie.note == null
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (movie.locationLabel != null) {
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
                    text = movie.locationLabel,
                    fontFamily = Outfit,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MsInk
                )
                movie.locationCoords?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontFamily = Outfit,
                        fontSize = 12.sp,
                        color = MsTeal
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        MsPrimaryButton(text = "Oznacz jako obejrzany", onClick = { /* później: update statusu */ })
        Spacer(modifier = Modifier.height(8.dp))
        MsGhostButton(text = "♥ Ulubione", onClick = { /* później: ulubione */ })
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
