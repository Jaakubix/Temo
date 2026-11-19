package com.movieshelf.app.data.repository

import com.movieshelf.app.BuildConfig
import com.movieshelf.app.data.api.RetrofitClient
import com.movieshelf.app.data.api.TmdbApiService
import com.movieshelf.app.data.model.Movie
import com.movieshelf.app.data.model.toMovie
import com.movieshelf.app.network.ApiResult
import com.movieshelf.app.network.NetworkMonitor
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Warstwa między API a ViewModelem.
 * Odpowiada za: sprawdzenie sieci, wywołanie TMDb, mapowanie DTO → Movie, tłumaczenie błędów.
 */
class MovieRepository(
    private val api: TmdbApiService = RetrofitClient.tmdbApi,
    private val networkMonitor: NetworkMonitor
) {

    /**
     * Wyszukuje filmy po tytule.
     * Zwraca [ApiResult] — nigdy nie rzuca wyjątku do UI.
     */
    suspend fun searchMovies(query: String, page: Int = 1): ApiResult<List<Movie>> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            return ApiResult.Success(emptyList())
        }

        // Brak klucza API — czytelny błąd zamiast 401 z TMDb
        if (BuildConfig.TMDB_API_KEY.isBlank()) {
            return ApiResult.Error(
                message = "Brak klucza TMDB_API_KEY w local.properties."
            )
        }

        // Offline — nie wołamy sieci w ogóle
        if (!networkMonitor.isOnline()) {
            return ApiResult.NoConnection
        }

        return try {
            val response = api.searchMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                query = trimmed,
                page = page
            )
            // Mapujemy listę DTO na modele domenowe
            val movies = response.results.map { it.toMovie() }
            ApiResult.Success(movies)
        } catch (e: HttpException) {
            // Błąd HTTP (4xx / 5xx) — komunikat zależny od kodu
            ApiResult.Error(
                message = mapHttpError(e.code()),
                code = e.code(),
                cause = e
            )
        } catch (e: SocketTimeoutException) {
            ApiResult.Error(
                message = "Przekroczono czas oczekiwania na odpowiedź serwera.",
                cause = e
            )
        } catch (e: UnknownHostException) {
            // DNS / host niedostępny — traktujemy jak brak sieci
            ApiResult.NoConnection
        } catch (e: IOException) {
            // Inne problemy I/O (np. zerwane połączenie w trakcie requestu)
            ApiResult.Error(
                message = "Problem z połączeniem sieciowym. Spróbuj ponownie.",
                cause = e
            )
        } catch (e: Exception) {
            ApiResult.Error(
                message = "Nie udało się pobrać wyników. Spróbuj ponownie.",
                cause = e
            )
        }
    }

    /** Proste, przyjazne komunikaty zamiast surowych kodów HTTP */
    private fun mapHttpError(code: Int): String = when (code) {
        401 -> "Nieprawidłowy klucz API TMDb (401)."
        404 -> "Nie znaleziono zasobu (404)."
        429 -> "Zbyt wiele zapytań — odczekaj chwilę (429)."
        in 500..599 -> "Serwer TMDb ma problemy ($code). Spróbuj później."
        else -> "Błąd serwera (HTTP $code)."
    }
}
