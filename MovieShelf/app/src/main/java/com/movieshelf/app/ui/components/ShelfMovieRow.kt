package com.movieshelf.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.movieshelf.app.data.model.ShelfMovie
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.Outfit
import com.movieshelf.app.ui.theme.SpaceGrotesk

/** Karta filmu na półce — dane z Room */
@Composable
fun ShelfMovieRow(
    movie: ShelfMovie,
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
        ShelfPoster(movie = movie, width = 56.dp, height = 78.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MsInk,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = listOfNotNull(movie.year, movie.genre).joinToString(" · "),
                fontFamily = Outfit,
                fontSize = 13.sp,
                color = MsInkSoft
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = movie.statusLabel,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(androidx.compose.ui.graphics.Color(0xFFEEF2F6))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                fontFamily = Outfit,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = MsInkSoft
            )
        }
    }
}

@Composable
fun ShelfPoster(
    movie: ShelfMovie,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    corner: androidx.compose.ui.unit.Dp = 10.dp
) {
    val url = movie.posterUrl()
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(corner))
                .background(MsLine)
        )
    } else {
        PosterPlaceholder(
            styleIndex = movie.posterStyle,
            width = width,
            height = height,
            corner = corner
        )
    }
}
