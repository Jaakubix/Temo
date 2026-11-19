package com.movieshelf.app.ui.navigation

/**
 * Trasy nawigacji — id filmu to lokalny klucz Room (Long).
 */
sealed interface AppRoute {
    data object Login : AppRoute
    data object Shelf : AppRoute
    data object Shaker : AppRoute
    data object Search : AppRoute
    data class Detail(val movieId: Long) : AppRoute
    data class Edit(val movieId: Long) : AppRoute
}
