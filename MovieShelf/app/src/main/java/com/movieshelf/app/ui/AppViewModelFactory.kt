package com.movieshelf.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieshelf.app.MovieShelfApplication
import com.movieshelf.app.ui.auth.AuthViewModel
import com.movieshelf.app.ui.detail.DetailViewModel
import com.movieshelf.app.ui.edit.EditViewModel
import com.movieshelf.app.ui.search.SearchViewModel
import com.movieshelf.app.ui.shaker.ShakerViewModel
import com.movieshelf.app.ui.shelf.ShelfViewModel

/**
 * Fabryka ViewModeli — wstrzykuje repozytoria z [AppContainer].
 */
class AppViewModelFactory(
    private val app: MovieShelfApplication,
    private val movieId: Long = 0L
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val c = app.container
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(c.authRepository, c.networkMonitor) as T

            modelClass.isAssignableFrom(ShelfViewModel::class.java) ->
                ShelfViewModel(c.shelfRepository, c.networkMonitor) as T

            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(c.movieRepository, c.shelfRepository, c.networkMonitor) as T

            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(movieId, c.shelfRepository) as T

            modelClass.isAssignableFrom(EditViewModel::class.java) ->
                EditViewModel(movieId, c.shelfRepository) as T

            modelClass.isAssignableFrom(ShakerViewModel::class.java) ->
                ShakerViewModel(c.shelfRepository) as T

            else -> error("Nieznany ViewModel: ${modelClass.name}")
        }
    }
}
