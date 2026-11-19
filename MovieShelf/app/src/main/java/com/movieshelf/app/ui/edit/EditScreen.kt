package com.movieshelf.app.ui.edit

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.data.demo.demoMovieById
import com.movieshelf.app.ui.components.BrandLabel
import com.movieshelf.app.ui.components.MsDangerButton
import com.movieshelf.app.ui.components.MsTextButton
import com.movieshelf.app.ui.components.ScreenTitle
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

/** Ekran edycji — status, ocena, notatka, GPS, usuwanie (jak makieta) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    movieId: Int,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = demoMovieById(movieId)
    val statuses = listOf("Do obejrzenia", "Obejrzany", "Ulubiony")
    var statusIndex by remember { mutableStateOf(0) }
    var rating by remember { mutableFloatStateOf(movie.rating.coerceIn(1f, 5f).takeIf { it > 0 } ?: 4f) }
    var note by remember { mutableStateOf(movie.note.orEmpty()) }
    var placeLabel by remember { mutableStateOf(movie.locationLabel ?: "Kino Helios · Gdańsk") }
    var gpsHint by remember { mutableStateOf("Użyj GPS, żeby zapisać miejsce oglądania.") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MsBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MsTextButton(text = "← Anuluj", onClick = onCancel)
            MsTextButton(text = "Zapisz", onClick = onSave, accent = true)
        }

        BrandLabel()
        Spacer(modifier = Modifier.height(4.dp))
        ScreenTitle("Edycja")
        Spacer(modifier = Modifier.height(16.dp))

        FieldLabel("Status")
        Spacer(modifier = Modifier.height(6.dp))
        // Prosty „select” jako 3 chipy — bez natywnego Spinnera
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            statuses.forEachIndexed { index, label ->
                val active = index == statusIndex
                Text(
                    text = label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) MsInk else MsBgElevated)
                        .border(1.dp, if (active) MsInk else MsLine, RoundedCornerShape(12.dp))
                        .clickable { statusIndex = index }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    color = if (active) MsBgElevated else MsInk,
                    fontFamily = Outfit,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        FieldLabel("Ocena (1–5)  ·  ${rating.toInt()}")
        Slider(
            value = rating,
            onValueChange = { rating = it },
            valueRange = 1f..5f,
            steps = 3,
            colors = SliderDefaults.colors(
                thumbColor = MsAmber,
                activeTrackColor = MsAmber,
                inactiveTrackColor = MsLine
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        FieldLabel("Notatka")
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Co zapamiętać po seansie…", color = MsInkSoft) },
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MsBgElevated,
                unfocusedContainerColor = MsBgElevated,
                focusedIndicatorColor = MsAmber,
                unfocusedIndicatorColor = MsLine,
                cursorColor = MsAmber,
                focusedTextColor = MsInk,
                unfocusedTextColor = MsInk
            )
        )

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MsTealSoft, RoundedCornerShape(18.dp))
                .border(1.dp, MsTeal.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Lokalizacja seansu",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MsInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = gpsHint,
                    fontFamily = Outfit,
                    fontSize = 12.sp,
                    color = MsInkSoft
                )
            }
            Text(
                text = "Pobierz GPS",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MsInk)
                    .clickable {
                        // Placeholder — prawdziwy GPS dołożymy z sensorami
                        gpsHint = "54.352 °N, 18.646 °E"
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = MsBgElevated,
                fontFamily = Outfit,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        FieldLabel("Etykieta miejsca")
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = placeLabel,
            onValueChange = { placeLabel = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("np. kanapa w salonie, Helios", color = MsInkSoft) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MsBgElevated,
                unfocusedContainerColor = MsBgElevated,
                focusedIndicatorColor = MsAmber,
                unfocusedIndicatorColor = MsLine,
                cursorColor = MsAmber,
                focusedTextColor = MsInk,
                unfocusedTextColor = MsInk
            )
        )

        Spacer(modifier = Modifier.height(18.dp))
        MsDangerButton(text = "Usuń z półki", onClick = onDelete)
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontFamily = Outfit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        color = MsInkSoft
    )
}
