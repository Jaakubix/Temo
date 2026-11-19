package com.movieshelf.app.ui.shelf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.ui.components.AmberFab
import com.movieshelf.app.ui.components.BottomTab
import com.movieshelf.app.ui.components.BrandLabel
import com.movieshelf.app.ui.components.MovieShelfBottomBar
import com.movieshelf.app.ui.components.ScreenTitle
import com.movieshelf.app.ui.components.ShelfMovieRow
import com.movieshelf.app.ui.components.ShelfTabs
import com.movieshelf.app.ui.theme.MsBg
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.MsTeal
import com.movieshelf.app.ui.theme.MsTealSoft
import com.movieshelf.app.ui.theme.Outfit

/**
 * Półka z danymi z Room — działa offline.
 * Zakładki filtrują lokalny stan (WATCHLIST / WATCHED / ulubione).
 */
@Composable
fun ShelfScreen(
    viewModel: ShelfViewModel,
    userEmail: String?,
    onOpenDetail: (Long) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenShaker: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val allMovies by viewModel.movies.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val filtered = remember(tabIndex, allMovies) {
        viewModel.filter(tabIndex, allMovies)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MsBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BrandLabel()
                    Spacer(modifier = Modifier.height(2.dp))
                    ScreenTitle("Twoja półka")
                    if (!userEmail.isNullOrBlank()) {
                        Text(
                            text = userEmail,
                            fontFamily = Outfit,
                            fontSize = 12.sp,
                            color = MsInkSoft
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onSignOut) {
                        Text("Wyloguj", color = MsInkSoft, fontFamily = Outfit, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MsBgElevated)
                            .border(1.dp, MsLine, RoundedCornerShape(12.dp))
                            .clickable(onClick = onOpenShaker),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("◎", color = MsInk, fontSize = 18.sp)
                    }
                }
            }

            if (!isOnline) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Tryb offline — półka z lokalnej bazy, wyszukiwanie TMDb niedostępne.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MsTealSoft, RoundedCornerShape(12.dp))
                        .border(1.dp, MsTeal.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    fontFamily = Outfit,
                    fontSize = 12.sp,
                    color = MsInk
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            ShelfTabs(selectedIndex = tabIndex, onSelect = { tabIndex = it })
            Spacer(modifier = Modifier.height(14.dp))

            if (filtered.isEmpty()) {
                Text(
                    text = when (tabIndex) {
                        1 -> "Brak obejrzanych — oznacz film w szczegółach."
                        2 -> "Brak ulubionych — dodaj serduszkiem w szczegółach."
                        else -> "Półka pusta — dodaj film przez „+” lub wyszukiwarkę TMDb."
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    fontFamily = Outfit,
                    fontSize = 14.sp,
                    color = MsInkSoft,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(filtered, key = { it.id }) { movie ->
                        ShelfMovieRow(
                            movie = movie,
                            onClick = { onOpenDetail(movie.id) }
                        )
                    }
                }
            }
        }

        AmberFab(
            onClick = onOpenSearch,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 86.dp)
        )

        MovieShelfBottomBar(
            active = BottomTab.SHELF,
            onShelf = {},
            onShaker = onOpenShaker,
            onAdd = onOpenSearch,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
