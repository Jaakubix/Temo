package com.movieshelf.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.movieshelf.app.MovieShelfApplication
import com.movieshelf.app.ui.AppViewModelFactory
import com.movieshelf.app.ui.auth.AuthViewModel
import com.movieshelf.app.ui.auth.LoginScreen
import com.movieshelf.app.ui.detail.DetailScreen
import com.movieshelf.app.ui.detail.DetailViewModel
import com.movieshelf.app.ui.edit.EditScreen
import com.movieshelf.app.ui.edit.EditViewModel
import com.movieshelf.app.ui.search.SearchScreen
import com.movieshelf.app.ui.search.SearchViewModel
import com.movieshelf.app.ui.shaker.ShakerScreen
import com.movieshelf.app.ui.shaker.ShakerViewModel
import com.movieshelf.app.ui.shelf.ShelfScreen
import com.movieshelf.app.ui.shelf.ShelfViewModel

/**
 * Host nawigacji + bramka Firebase Auth.
 * Po zalogowaniu półka korzysta z Room (offline).
 */
@Composable
fun MovieShelfApp(modifier: Modifier = Modifier) {
    val app = LocalContext.current.applicationContext as MovieShelfApplication
    val authFactory = remember { AppViewModelFactory(app) }
    val authViewModel: AuthViewModel = viewModel(factory = authFactory)
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (!isLoggedIn) {
        LoginScreen(viewModel = authViewModel, modifier = modifier)
        return
    }

    var route by remember { mutableStateOf<AppRoute>(AppRoute.Shelf) }
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
        backStack = emptyList()
        route = target
    }

    when (val current = route) {
        AppRoute.Login -> LoginScreen(viewModel = authViewModel, modifier = modifier)

        AppRoute.Shelf -> {
            val shelfVm: ShelfViewModel = viewModel(factory = authFactory)
            ShelfScreen(
                viewModel = shelfVm,
                userEmail = authViewModel.userEmail,
                onOpenDetail = { id -> navigate(AppRoute.Detail(id)) },
                onOpenSearch = { goHome(AppRoute.Search) },
                onOpenShaker = { goHome(AppRoute.Shaker) },
                onSignOut = {
                    authViewModel.signOut()
                    goHome(AppRoute.Shelf)
                },
                modifier = modifier
            )
        }

        AppRoute.Shaker -> {
            val shakerVm: ShakerViewModel = viewModel(factory = authFactory)
            ShakerScreen(
                viewModel = shakerVm,
                onBack = { goHome(AppRoute.Shelf) },
                onOpenShelf = { goHome(AppRoute.Shelf) },
                onOpenSearch = { goHome(AppRoute.Search) },
                modifier = modifier
            )
        }

        AppRoute.Search -> {
            val searchVm: SearchViewModel = viewModel(factory = authFactory)
            SearchScreen(
                viewModel = searchVm,
                onCancel = { goHome(AppRoute.Shelf) },
                onMovieAdded = { id ->
                    backStack = emptyList()
                    route = AppRoute.Detail(id)
                },
                modifier = modifier
            )
        }

        is AppRoute.Detail -> {
            val detailFactory = remember(current.movieId) {
                AppViewModelFactory(app, current.movieId)
            }
            val detailVm: DetailViewModel = viewModel(
                key = "detail-${current.movieId}",
                factory = detailFactory
            )
            DetailScreen(
                viewModel = detailVm,
                onBack = { goBack() },
                onEdit = { id -> navigate(AppRoute.Edit(id)) },
                modifier = modifier
            )
        }

        is AppRoute.Edit -> {
            val editFactory = remember(current.movieId) {
                AppViewModelFactory(app, current.movieId)
            }
            val editVm: EditViewModel = viewModel(
                key = "edit-${current.movieId}",
                factory = editFactory
            )
            EditScreen(
                viewModel = editVm,
                onCancel = { goBack() },
                onSaved = { goBack() },
                onDeleted = { goHome(AppRoute.Shelf) },
                modifier = modifier
            )
        }
    }
}
