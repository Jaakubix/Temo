package com.movieshelf.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Gradientowe „plakaty” jak w makiecie (klasy .p1–.p6 w styles.css).
 * Używane dla danych demo, zanim będą prawdziwe URL-e z TMDb.
 */
private val PosterBrushes: List<Brush> = listOf(
    Brush.linearGradient(listOf(Color(0xFF3D2C1E), Color(0xFFC4A574), Color(0xFF1C3344))),
    Brush.linearGradient(listOf(Color(0xFF1A1A1A), Color(0xFF6B5A3E), Color(0xFFF0E6D2))),
    Brush.linearGradient(listOf(Color(0xFF4A1D6B), Color(0xFFE85D04), Color(0xFFF7B801))),
    Brush.linearGradient(listOf(Color(0xFF234E52), Color(0xFF81E6D9), Color(0xFFEFECBF))),
    Brush.linearGradient(listOf(Color(0xFF2C1810), Color(0xFF8B6914), Color(0xFFD6BC8A))),
    Brush.linearGradient(listOf(Color(0xFF1A365D), Color(0xFF718096), Color(0xFFE2E8F0)))
)

@Composable
fun PosterPlaceholder(
    styleIndex: Int,
    modifier: Modifier = Modifier,
    width: Dp = 56.dp,
    height: Dp = 78.dp,
    corner: Dp = 10.dp
) {
    val brush = PosterBrushes[styleIndex.mod(PosterBrushes.size)]
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .shadow(6.dp, RoundedCornerShape(corner), clip = false)
            .clip(RoundedCornerShape(corner))
            .background(brush)
    )
}
