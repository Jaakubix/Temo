package com.movieshelf.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Odpowiedź z endpointu wyszukiwania TMDb (/search/movie).
 * Pola odpowiadają JSON-owi z API — Gson mapuje je po @SerializedName.
 */
data class MovieSearchResponseDto(
    @SerializedName("page")
    val page: Int = 1,

    @SerializedName("results")
    val results: List<TmdbMovieDto> = emptyList(),

    @SerializedName("total_pages")
    val totalPages: Int = 0,

    @SerializedName("total_results")
    val totalResults: Int = 0
)

/**
 * Pojedynczy film w odpowiedzi TMDb (DTO = Data Transfer Object).
 * To surowy kształt z sieci — mapujemy go potem na model domenowy [Movie].
 */
data class TmdbMovieDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null
)
