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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.data.demo.DemoMovie
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.Outfit
import com.movieshelf.app.ui.theme.SpaceGrotesk

/** Karta filmu na półce — układ jak `.movie-row` w makiecie */
@Composable
fun ShelfMovieRow(
    movie: DemoMovie,
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
        PosterPlaceholder(styleIndex = movie.posterStyle)
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
                text = "${movie.year} · ${movie.genre}",
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
