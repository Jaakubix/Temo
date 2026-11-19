package com.movieshelf.app.ui.search

import com.movieshelf.app.data.model.Movie

/**
 * Stan ekranu wyszukiwania — UI renderuje jeden z wariantów.
 * Loading jest osobno od wyniku, żeby pokazać spinner bez kasowania poprzedniej listy
 * tylko gdy zaczynamy nowe wyszukiwanie od zera.
 */
sealed class SearchUiState {

    /** Stan początkowy — użytkownik jeszcze nic nie wpisał */
    data object Idle : SearchUiState()

    /** Trwa zapytanie do TMDb */
    data object Loading : SearchUiState()

    /** Sukces z niepustą listą */
    data class Success(val movies: List<Movie>) : SearchUiState()

    /** Sukces, ale API nic nie zwróciło dla danego zapytania */
    data object Empty : SearchUiState()

    /** Błąd sieciowy / HTTP / parsowania */
    data class Error(val message: String) : SearchUiState()

    /** Urządzenie offline */
    data object NoConnection : SearchUiState()
}
