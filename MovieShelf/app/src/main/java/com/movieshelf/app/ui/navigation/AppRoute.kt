package com.movieshelf.app.ui.navigation

/**
 * Trasy nawigacji odpowiadające ekranom z makiety UI.
 */
sealed interface AppRoute {
    data object Shelf : AppRoute
    data object Shaker : AppRoute
    data object Search : AppRoute
    data class Detail(val movieId: Int) : AppRoute
    data class Edit(val movieId: Int) : AppRoute
}
