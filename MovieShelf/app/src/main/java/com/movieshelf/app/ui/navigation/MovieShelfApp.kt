package com.movieshelf.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.movieshelf.app.ui.detail.DetailScreen
import com.movieshelf.app.ui.edit.EditScreen
import com.movieshelf.app.ui.search.SearchScreen
import com.movieshelf.app.ui.shaker.ShakerScreen
import com.movieshelf.app.ui.shelf.ShelfScreen

/**
 * Prosty host nawigacji bez Navigation Compose —
 * stany odpowiadają ekranom z makiety HTML.
 */
@Composable
fun MovieShelfApp(modifier: Modifier = Modifier) {
    var route by remember { mutableStateOf<AppRoute>(AppRoute.Shelf) }
    // Stos „wstecz” dla Detail/Edit, żeby Anuluj wracało sensownie
    var backStack by remember { mutableStateOf<List<AppRoute>>(emptyList()) }

    fun navigate(target: AppRoute) {
        backStack = backStack + route
        route = target
    }

    fun goBack() {
        val previous = backStack.lastOrNull()
        if (previous != null) {
            backStack = backStack.dropLast(1)
            route = previous
        } else {
            route = AppRoute.Shelf
        }
    }

    fun goHome(target: AppRoute) {
        // Przełączenia bottom bara — bez historii (jak w makiecie)
        backStack = emptyList()
        route = target
    }

    when (val current = route) {
        AppRoute.Shelf -> ShelfScreen(
            onOpenDetail = { id -> navigate(AppRoute.Detail(id)) },
            onOpenSearch = { goHome(AppRoute.Search) },
            onOpenShaker = { goHome(AppRoute.Shaker) },
            modifier = modifier
        )

        AppRoute.Shaker -> ShakerScreen(
            onBack = { goHome(AppRoute.Shelf) },
            onOpenShelf = { goHome(AppRoute.Shelf) },
            onOpenSearch = { goHome(AppRoute.Search) },
            modifier = modifier
        )

        AppRoute.Search -> SearchScreen(
            onCancel = { goHome(AppRoute.Shelf) },
            onMoviePicked = {
                // Po wyborze z API na razie otwieramy edycję demo-filmu
                navigate(AppRoute.Edit(1))
            },
            modifier = modifier
        )

        is AppRoute.Detail -> DetailScreen(
            movieId = current.movieId,
            onBack = { goBack() },
            onEdit = { id -> navigate(AppRoute.Edit(id)) },
            modifier = modifier
        )

        is AppRoute.Edit -> EditScreen(
            movieId = current.movieId,
            onCancel = { goBack() },
            onSave = { goBack() },
            onDelete = { goHome(AppRoute.Shelf) },
            modifier = modifier
        )
    }
}
