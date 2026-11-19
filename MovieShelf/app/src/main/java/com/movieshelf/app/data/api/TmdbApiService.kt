package com.movieshelf.app.data.api

import com.movieshelf.app.data.model.MovieSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Kontrakt REST dla The Movie Database (v3).
 * Bazowy URL ustawiamy w [RetrofitClient].
 *
 * Dokumentacja: https://developer.themoviedb.org/reference/search-movie
 */
interface TmdbApiService {

    /**
     * Wyszukiwanie filmów po tytule.
     * @param apiKey klucz z konta TMDb (BuildConfig.TMDB_API_KEY)
     * @param query fragment tytułu wpisany przez użytkownika
     * @param language język opisów i tytułów (pl-PL)
     * @param includeAdult czy zwracać treści 18+
     * @param page numer strony wyników (paginacja API)
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "pl-PL",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1
    ): MovieSearchResponseDto
}
