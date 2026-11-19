package com.movieshelf.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.movieshelf.app.data.model.MovieStatus
import com.movieshelf.app.data.model.ShelfMovie

/**
 * Wiersz tabeli `movies` w SQLite (Room).
 * Indeks na tmdbId — szybkie sprawdzanie „czy film już jest na półce”.
 */
@Entity(
    tableName = "movies",
    indices = [Index(value = ["tmdbId"], unique = false)]
)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tmdbId: Int? = null,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val year: String?,
    val genre: String,
    /** Nazwa enuma [MovieStatus] */
    val status: String,
    val isFavorite: Boolean,
    val rating: Float,
    val note: String?,
    val locationLabel: String?,
    val locationLat: Double?,
    val locationLng: Double?,
    val updatedAt: Long
)

fun MovieEntity.toShelfMovie(): ShelfMovie = ShelfMovie(
    id = id,
    tmdbId = tmdbId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    year = year,
    genre = genre,
    status = runCatching { MovieStatus.valueOf(status) }.getOrDefault(MovieStatus.WATCHLIST),
    isFavorite = isFavorite,
    rating = rating,
    note = note,
    locationLabel = locationLabel,
    locationLat = locationLat,
    locationLng = locationLng,
    updatedAt = updatedAt
)

fun ShelfMovie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    tmdbId = tmdbId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    year = year,
    genre = genre,
    status = status.name,
    isFavorite = isFavorite,
    rating = rating,
    note = note,
    locationLabel = locationLabel,
    locationLat = locationLat,
    locationLng = locationLng,
    updatedAt = updatedAt
)
