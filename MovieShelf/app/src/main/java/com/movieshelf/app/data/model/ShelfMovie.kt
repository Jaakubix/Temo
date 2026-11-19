package com.movieshelf.app.data.model

/**
 * Model filmu na lokalnej półce (UI + Room).
 * Osobny od [Movie] z TMDb — ma pola użytkownika: status, ocena, notatka, GPS.
 */
data class ShelfMovie(
    val id: Long = 0L,
    val tmdbId: Int? = null,
    val title: String,
    val overview: String = "",
    val posterPath: String? = null,
    val year: String? = null,
    val genre: String = "Film",
    val status: MovieStatus = MovieStatus.WATCHLIST,
    val isFavorite: Boolean = false,
    val rating: Float = 0f,
    val note: String? = null,
    val locationLabel: String? = null,
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val updatedAt: Long = System.currentTimeMillis()
) {
    val statusLabel: String
        get() = when {
            isFavorite && status == MovieStatus.WATCHED -> "Ulubiony · Obejrzany"
            isFavorite -> "Ulubiony"
            else -> status.toChipLabel()
        }

    /** Pełny URL plakatu TMDb albo null */
    fun posterUrl(size: String = "w342"): String? {
        val path = posterPath ?: return null
        return "https://image.tmdb.org/t/p/$size$path"
    }

    /** Stabilny indeks gradientu, gdy brak plakatu */
    val posterStyle: Int
        get() {
            val seed = (tmdbId?.toLong() ?: id)
            return ((seed % 6) + 6).toInt() % 6
        }
}

/** Tworzy wpis półki z wyniku wyszukiwania TMDb */
fun Movie.toShelfMovie(
    status: MovieStatus = MovieStatus.WATCHLIST
): ShelfMovie = ShelfMovie(
    tmdbId = tmdbId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    year = year,
    genre = TmdbGenres.label(genreIds),
    status = status,
    rating = voteAverage?.let { (it / 2.0).toFloat().coerceIn(0f, 5f) } ?: 0f
)
