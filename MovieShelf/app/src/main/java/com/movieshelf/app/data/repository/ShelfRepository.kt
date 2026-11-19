package com.movieshelf.app.data.repository

import com.movieshelf.app.data.local.MovieDao
import com.movieshelf.app.data.local.toEntity
import com.movieshelf.app.data.local.toShelfMovie
import com.movieshelf.app.data.model.Movie
import com.movieshelf.app.data.model.MovieStatus
import com.movieshelf.app.data.model.ShelfMovie
import com.movieshelf.app.data.model.toShelfMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * CRUD lokalnej półki (Room).
 * Wszystkie operacje działają offline — nie wymagają internetu.
 * Flow z DAO = wspólne źródło prawdy dla całego UI (globalny stan listy).
 */
class ShelfRepository(
    private val dao: MovieDao
) {

    /** Cała półka — obserwowana na żywo */
    fun observeAll(): Flow<List<ShelfMovie>> =
        dao.observeAll().map { list -> list.map { it.toShelfMovie() } }

    fun observeById(id: Long): Flow<ShelfMovie?> =
        dao.observeById(id).map { it?.toShelfMovie() }

    suspend fun getById(id: Long): ShelfMovie? =
        dao.getById(id)?.toShelfMovie()

    /**
     * Dodaje wynik z TMDb na półkę.
     * Jeśli film o tym tmdbId już istnieje — zwraca istniejące id (bez duplikatu).
     */
    suspend fun addFromTmdb(movie: Movie): Long {
        movie.tmdbId.let { tmdbId ->
            val existing = dao.getByTmdbId(tmdbId)
            if (existing != null) return existing.id
        }
        val entity = movie.toShelfMovie().copy(
            updatedAt = System.currentTimeMillis()
        ).toEntity().copy(id = 0)
        return dao.insert(entity)
    }

    /** Ręczne dodanie pustego wpisu (bez API) */
    suspend fun addManual(title: String = "Nowy film"): Long {
        val entity = ShelfMovie(
            title = title,
            status = MovieStatus.WATCHLIST,
            updatedAt = System.currentTimeMillis()
        ).toEntity().copy(id = 0)
        return dao.insert(entity)
    }

    suspend fun update(movie: ShelfMovie) {
        dao.update(
            movie.copy(updatedAt = System.currentTimeMillis()).toEntity()
        )
    }

    suspend fun markWatched(id: Long) {
        val current = dao.getById(id) ?: return
        dao.update(
            current.copy(
                status = MovieStatus.WATCHED.name,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleFavorite(id: Long) {
        val current = dao.getById(id) ?: return
        dao.update(
            current.copy(
                isFavorite = !current.isFavorite,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    /** Lista „Do obejrzenia” — do Movie Shakera */
    suspend fun getWatchlist(): List<ShelfMovie> =
        dao.getWatchlist().map { it.toShelfMovie() }
}
