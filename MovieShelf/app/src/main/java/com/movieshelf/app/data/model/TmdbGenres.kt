package com.movieshelf.app.data.model

/** Prosta mapa ID gatunków TMDb → polska etykieta (bez dodatkowego requestu) */
object TmdbGenres {
    private val map = mapOf(
        28 to "Akcja",
        12 to "Przygodowy",
        16 to "Animacja",
        35 to "Komedia",
        80 to "Kryminał",
        99 to "Dokument",
        18 to "Dramat",
        10751 to "Familijny",
        14 to "Fantasy",
        36 to "Historyczny",
        27 to "Horror",
        10402 to "Muzyczny",
        9648 to "Mystery",
        10749 to "Romans",
        878 to "Sci-Fi",
        53 to "Thriller",
        10752 to "Wojenny",
        37 to "Western",
        10770 to "TV"
    )

    fun label(ids: List<Int>): String =
        ids.firstNotNullOfOrNull { map[it] } ?: "Film"
}
