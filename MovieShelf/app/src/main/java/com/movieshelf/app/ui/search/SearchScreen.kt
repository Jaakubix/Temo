package com.movieshelf.app.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.ui.components.BrandLabel
import com.movieshelf.app.ui.components.EmptyResultsPanel
import com.movieshelf.app.ui.components.ErrorPanel
import com.movieshelf.app.ui.components.LoadingPanel
import com.movieshelf.app.ui.components.MovieSearchRow
import com.movieshelf.app.ui.components.MsGhostButton
import com.movieshelf.app.ui.components.MsTextButton
import com.movieshelf.app.ui.components.NoConnectionPanel
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

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onCancel: () -> Unit,
    onMovieAdded: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val query by viewModel.query.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.addedMovieId.collect { id ->
            onMovieAdded(id)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MsBg)
            .padding(horizontal = 16.dp)
    ) {
        MsTextButton(text = "← Anuluj", onClick = onCancel)

        BrandLabel()
        Spacer(modifier = Modifier.height(4.dp))
        ScreenTitle("Dodaj film")
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Wyszukaj w TMDb — po dodaniu film trafia do lokalnej bazy (offline).",
            fontFamily = Outfit,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MsInkSoft
        )

        if (!isOnline) {
            Spacer(modifier = Modifier.height(12.dp))
            OfflineBanner()
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("np. Dune, Parasite…", color = MsInkSoft, fontFamily = Outfit)
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MsBgElevated,
                unfocusedContainerColor = MsBgElevated,
                focusedBorderColor = MsAmber,
                unfocusedBorderColor = MsLine,
                cursorColor = MsAmber,
                focusedTextColor = MsInk,
                unfocusedTextColor = MsInk
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        when (val state = uiState) {
            SearchUiState.Idle -> {
                Text(
                    text = "Wpisz tytuł, żeby wyszukać film.",
                    color = MsInkSoft,
                    fontFamily = Outfit,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            SearchUiState.Loading -> LoadingPanel(
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            is SearchUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(state.movies, key = { it.tmdbId }) { movie ->
                        MovieSearchRow(
                            movie = movie,
                            onClick = { viewModel.addMovie(movie) }
                        )
                    }
                }
            }

            SearchUiState.Empty -> {
                EmptyResultsPanel(modifier = Modifier.padding(top = 8.dp))
                Spacer(modifier = Modifier.weight(1f))
            }

            is SearchUiState.Error -> {
                ErrorPanel(
                    message = state.message,
                    onRetry = viewModel::retry,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            SearchUiState.NoConnection -> {
                NoConnectionPanel(
                    onRetry = viewModel::retry,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        MsGhostButton(
            text = "Dodaj ręcznie bez API",
            onClick = viewModel::addManual,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }
}

@Composable
private fun OfflineBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MsTealSoft, RoundedCornerShape(12.dp))
            .border(1.dp, MsTeal.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Outlined.WifiOff, contentDescription = null, tint = MsTeal)
        Text(
            text = "Offline — możesz dodać film ręcznie; TMDb wymaga sieci.",
            color = MsInk,
            fontFamily = Outfit,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
