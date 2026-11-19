package com.movieshelf.app.data.demo

/**
 * Dane demonstracyjne z makiety UI (te same tytuły / opisy).
 * Docelowo zastąpi je lokalna baza Room — na razie służą do wyglądu ekranów.
 */
data class DemoMovie(
    val id: Int,
    val title: String,
    val year: String,
    val genre: String,
    val runtimeMinutes: Int? = null,
    val overview: String,
    val statusLabel: String,
    val status: DemoStatus,
    val rating: Float = 0f,
    val note: String? = null,
    val locationLabel: String? = null,
    val locationCoords: String? = null,
    /** Indeks gradientu plakatu (jak klasy p1–p6 w styles.css) */
    val posterStyle: Int
)

enum class DemoStatus { WATCHLIST, WATCHED, FAVORITE }

val DemoMovies: List<DemoMovie> = listOf(
    DemoMovie(
        id = 1,
        title = "Dune: Part Two",
        year = "2024",
        genre = "Sci-Fi",
        runtimeMinutes = 166,
        overview = "Paul Atreides łączy siły z Chani i Fremenami, by pomścić rodzinę i zapobiec straszliwej przyszłości, którą tylko on potrafi przewidzieć.",
        statusLabel = "Wishlist",
        status = DemoStatus.WATCHLIST,
        rating = 4f,
        locationLabel = "Kino Helios · Gdańsk",
        locationCoords = "54.352 °N, 18.646 °E · zapisane GPS",
        posterStyle = 0
    ),
    DemoMovie(
        id = 2,
        title = "Oppenheimer",
        year = "2023",
        genre = "Biograficzny",
        runtimeMinutes = 180,
        overview = "Historia J. Roberta Oppenheimera i projektu Manhattan.",
        statusLabel = "Wishlist",
        status = DemoStatus.WATCHLIST,
        rating = 5f,
        posterStyle = 1
    ),
    DemoMovie(
        id = 3,
        title = "Everything Everywhere",
        year = "2022",
        genre = "Akcja",
        overview = "Evelyn Wang odkrywa multiversum i musi uratować świat.",
        statusLabel = "Wishlist",
        status = DemoStatus.WATCHLIST,
        posterStyle = 2
    ),
    DemoMovie(
        id = 4,
        title = "Past Lives",
        year = "2023",
        genre = "Dramat",
        overview = "Dwoje przyjaciół z dzieciństwa spotyka się po latach.",
        statusLabel = "Wishlist",
        status = DemoStatus.WATCHLIST,
        posterStyle = 3
    )
)

fun demoMovieById(id: Int): DemoMovie =
    DemoMovies.firstOrNull { it.id == id } ?: DemoMovies.first()
