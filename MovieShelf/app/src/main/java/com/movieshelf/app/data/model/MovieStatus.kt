package com.movieshelf.app.data.model

/**
 * Status filmu na półce.
 * Ulubione są osobną flagą [ShelfMovie.isFavorite], żeby film mógł być
 * jednocześnie „obejrzany” i „ulubiony”.
 */
enum class MovieStatus {
    WATCHLIST,
    WATCHED
}

fun MovieStatus.toChipLabel(): String = when (this) {
    MovieStatus.WATCHLIST -> "Do obejrzenia"
    MovieStatus.WATCHED -> "Obejrzany"
}
