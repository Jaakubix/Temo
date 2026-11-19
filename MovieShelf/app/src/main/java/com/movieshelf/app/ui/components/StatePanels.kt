package com.movieshelf.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.ui.theme.MsAmber
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.MsTeal
import com.movieshelf.app.ui.theme.MsTealSoft
import com.movieshelf.app.ui.theme.Outfit
import com.movieshelf.app.ui.theme.SpaceGrotesk

/** Spinner ładowania wyników z API */
@Composable
fun LoadingPanel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MsAmber)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Szukam filmów…",
            color = MsInkSoft,
            fontFamily = Outfit,
            textAlign = TextAlign.Center
        )
    }
}

/** Panel braku połączenia z internetem */
@Composable
fun NoConnectionPanel(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    StatusPanel(
        icon = Icons.Outlined.CloudOff,
        title = "Brak połączenia",
        body = "Sprawdź Wi‑Fi lub dane mobilne i spróbuj ponownie.",
        accentTeal = true,
        onRetry = onRetry,
        modifier = modifier
    )
}

/** Panel błędu API / HTTP / timeout */
@Composable
fun ErrorPanel(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    StatusPanel(
        icon = Icons.Outlined.ErrorOutline,
        title = "Coś poszło nie tak",
        body = message,
        accentTeal = false,
        onRetry = onRetry,
        modifier = modifier
    )
}

/** Puste wyniki wyszukiwania */
@Composable
fun EmptyResultsPanel(modifier: Modifier = Modifier) {
    StatusPanel(
        icon = Icons.Outlined.SearchOff,
        title = "Brak wyników",
        body = "Spróbuj innej frazy albo sprawdź pisownię tytułu.",
        accentTeal = false,
        onRetry = null,
        modifier = modifier
    )
}

/** Wspólny układ paneli statusu (offline / błąd / pusto) */
@Composable
private fun StatusPanel(
    icon: ImageVector,
    title: String,
    body: String,
    accentTeal: Boolean,
    onRetry: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .background(
                color = if (accentTeal) MsTealSoft else MsBgElevated,
                shape = shape
            )
            .border(
                width = 1.dp,
                color = if (accentTeal) MsTeal.copy(alpha = 0.35f) else MsLine,
                shape = shape
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (accentTeal) MsTeal else MsAmber,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            color = MsInk,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = body,
            color = MsInkSoft,
            fontFamily = Outfit,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MsInk,
                    contentColor = MsBgElevated
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Spróbuj ponownie", fontFamily = Outfit, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
