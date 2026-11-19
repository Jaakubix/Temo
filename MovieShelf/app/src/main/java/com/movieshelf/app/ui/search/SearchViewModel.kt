package com.movieshelf.app.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.movieshelf.app.data.repository.MovieRepository
import com.movieshelf.app.network.ApiResult
import com.movieshelf.app.network.NetworkMonitor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Logika ekranu „Dodaj film”.
 * - debounce: nie odpytujemy API po każdej literze
 * - anulowanie poprzedniego Job: szybkie wpisywanie nie miesza wyników
 * - obserwacja sieci: gdy wraca internet, możemy odświeżyć ostatnie zapytanie
 */
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val networkMonitor = NetworkMonitor(application)
    private val repository = MovieRepository(networkMonitor = networkMonitor)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    /** Czy urządzenie ma teraz internet (do paska / banneru) */
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private var searchJob: Job? = null

    init {
        // Subskrypcja ConnectivityManager — czyszczona automatycznie z viewModelScope
        viewModelScope.launch {
            networkMonitor.connectivityFlow().collect { online ->
                _isOnline.value = online
                // Jeśli byliśmy na ekranie „brak sieci” i net wrócił — powtórz ostatnie wyszukiwanie
                if (online && _uiState.value is SearchUiState.NoConnection) {
                    val currentQuery = _query.value
                    if (currentQuery.isNotBlank()) {
                        performSearch(currentQuery, debounceMs = 0L)
                    }
                }
            }
        }
    }

    /** Wywołanie z pola tekstowego (Compose TextField) */
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isBlank()) {
            searchJob?.cancel()
            _uiState.value = SearchUiState.Idle
            return
        }
        performSearch(newQuery)
    }

    /** Przycisk „Spróbuj ponownie” na ekranie błędu / offline */
    fun retry() {
        val currentQuery = _query.value
        if (currentQuery.isNotBlank()) {
            performSearch(currentQuery, debounceMs = 0L)
        }
    }

    /**
     * Uruchamia wyszukiwanie z opcjonalnym opóźnieniem (debounce).
     * Anuluje poprzedni Job, żeby nie nadpisać UI starszą odpowiedzią.
     */
    private fun performSearch(rawQuery: String, debounceMs: Long = 400L) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (debounceMs > 0) {
                delay(debounceMs)
            }
            // Po debounce sprawdzamy, czy query nadal aktualne
            if (_query.value.trim() != rawQuery.trim()) return@launch

            _uiState.value = SearchUiState.Loading

            when (val result = repository.searchMovies(rawQuery)) {
                is ApiResult.Success -> {
                    _uiState.value = if (result.data.isEmpty()) {
                        SearchUiState.Empty
                    } else {
                        SearchUiState.Success(result.data)
                    }
                }
                is ApiResult.Error -> {
                    _uiState.value = SearchUiState.Error(result.message)
                }
                ApiResult.NoConnection -> {
                    _uiState.value = SearchUiState.NoConnection
                }
            }
        }
    }

    // AndroidViewModel czyści viewModelScope przy onCleared — Job i Flow się anulują.
    // Nie trzymamy tu dodatkowych listenerów wymagających ręcznego dispose.
}
