package com.movieshelf.app.ui.shelf

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.data.demo.DemoMovies
import com.movieshelf.app.data.demo.DemoStatus
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
import com.movieshelf.app.ui.theme.Outfit

/**
 * Ekran „Półka” — 1:1 z makietą (zakładki, lista, FAB, bottom bar).
 */
@Composable
fun ShelfScreen(
    onOpenDetail: (Int) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenShaker: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tabIndex by remember { mutableIntStateOf(0) }

    // Filtrowanie demo-danych wg zakładki (na razie wszystkie są WATCHLIST)
    val filtered = remember(tabIndex) {
        when (tabIndex) {
            1 -> DemoMovies.filter { it.status == DemoStatus.WATCHED }
            2 -> DemoMovies.filter { it.status == DemoStatus.FAVORITE }
            else -> DemoMovies.filter { it.status == DemoStatus.WATCHLIST }
        }
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
                Column {
                    BrandLabel()
                    Spacer(modifier = Modifier.height(2.dp))
                    ScreenTitle("Twoja półka")
                }
                // Przycisk skrótu do Shakera (◎ w makiecie)
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

            Spacer(modifier = Modifier.height(14.dp))
            ShelfTabs(selectedIndex = tabIndex, onSelect = { tabIndex = it })
            Spacer(modifier = Modifier.height(14.dp))

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
                item {
                    Text(
                        text = "Lazy loading · kolejne 10 pozycji…",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontFamily = Outfit,
                        fontSize = 12.sp,
                        color = MsInkSoft,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
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
