package com.movieshelf.app.data.model

/**
 * Model domenowy filmu używany w UI i (później) w lokalnej bazie.
 * Niezależny od kształtu JSON-a TMDb — łatwiej go rozszerzać o ocenę, notatkę, GPS itd.
 */
data class Movie(
    /** Identyfikator z The Movie Database */
    val tmdbId: Int,
    val title: String,
    val overview: String,
    /** Relatywna ścieżka plakatu z TMDb, np. "/abc123.jpg" (bez hosta) */
    val posterPath: String?,
    /** Data premiery w formacie yyyy-MM-dd albo null */
    val releaseDate: String?,
    val genreIds: List<Int>,
    /** Średnia ocena TMDb (0–10) */
    val voteAverage: Double?
) {
    /** Rok z daty premiery — wygodne do wyświetlenia na liście */
    val year: String?
        get() = releaseDate
            ?.takeIf { it.length >= 4 }
            ?.substring(0, 4)

    /**
     * Pełny URL plakatu do Coil / Image.
     * @param size rozmiar z katalogu TMDb: w185, w342, w500, original…
     */
    fun posterUrl(size: String = "w342"): String? {
        val path = posterPath ?: return null
        return "https://image.tmdb.org/t/p/$size$path"
    }
}

/**
 * Mapowanie DTO z API → model aplikacji.
 * Trzymamy to przy modelu, żeby warstwa sieciowa nie znała szczegółów UI.
 */
fun TmdbMovieDto.toMovie(): Movie = Movie(
    tmdbId = id,
    title = title?.takeIf { it.isNotBlank() } ?: "Bez tytułu",
    overview = overview.orEmpty(),
    posterPath = posterPath,
    releaseDate = releaseDate?.takeIf { it.isNotBlank() },
    genreIds = genreIds.orEmpty(),
    voteAverage = voteAverage
)
