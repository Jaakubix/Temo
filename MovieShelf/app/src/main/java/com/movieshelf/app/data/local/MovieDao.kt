package com.movieshelf.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Operacje CRUD na lokalnej półce.
 * Flow = automatyczne odświeżanie UI przy każdej zmianie w bazie (globalny stan).
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<MovieEntity?>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MovieEntity?

    @Query("SELECT * FROM movies WHERE tmdbId = :tmdbId LIMIT 1")
    suspend fun getByTmdbId(tmdbId: Int): MovieEntity?

    @Query("SELECT * FROM movies WHERE status = :status ORDER BY updatedAt DESC")
    fun observeByStatus(status: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun observeFavorites(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE status = 'WATCHLIST' ORDER BY updatedAt DESC")
    suspend fun getWatchlist(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MovieEntity): Long

    @Update
    suspend fun update(entity: MovieEntity)

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun count(): Int
}
