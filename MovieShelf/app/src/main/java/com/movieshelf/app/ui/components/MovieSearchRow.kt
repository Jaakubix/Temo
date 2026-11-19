package com.movieshelf.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.movieshelf.app.data.model.Movie
import com.movieshelf.app.ui.theme.MsAmber
import com.movieshelf.app.ui.theme.MsAmberSoft
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.Outfit
import com.movieshelf.app.ui.theme.SpaceGrotesk

/** Wiersz wyniku TMDb — jak `.search-row` w makiecie */
@Composable
fun MovieSearchRow(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MsBgElevated)
            .border(1.dp, MsLine, shape)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterThumb(posterUrl = movie.posterUrl())
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.SemiBold,
                color = MsInk,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = movie.year ?: "Rok nieznany",
                fontFamily = Outfit,
                color = MsInkSoft,
                fontSize = 13.sp
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MsAmberSoft)
                .padding(horizontal = 8.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = MsAmber,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Outfit
            )
        }
    }
}

@Composable
private fun PosterThumb(posterUrl: String?) {
    val shape = RoundedCornerShape(10.dp)
    if (posterUrl != null) {
        AsyncImage(
            model = posterUrl,
            contentDescription = "Plakat filmu",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(44.dp)
                .height(62.dp)
                .clip(shape)
                .background(MsLine)
        )
    } else {
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(62.dp)
                .clip(shape)
                .background(MsLine),
            contentAlignment = Alignment.Center
        ) {
            Text("—", color = MsInkSoft, fontFamily = Outfit)
        }
    }
}
