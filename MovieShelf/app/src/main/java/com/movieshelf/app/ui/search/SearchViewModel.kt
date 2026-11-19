package com.movieshelf.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieshelf.app.data.model.Movie
import com.movieshelf.app.data.repository.MovieRepository
import com.movieshelf.app.data.repository.ShelfRepository
import com.movieshelf.app.network.ApiResult
import com.movieshelf.app.network.NetworkMonitor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Wyszukiwanie TMDb + dodawanie na lokalną półkę (Room).
 */
class SearchViewModel(
    private val movieRepository: MovieRepository,
    private val shelfRepository: ShelfRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    /** Emisja id nowo dodanego filmu — nawigacja do szczegółów */
    private val _addedMovieId = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val addedMovieId: SharedFlow<Long> = _addedMovieId.asSharedFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            networkMonitor.connectivityFlow().collect { online ->
                _isOnline.value = online
                if (online && _uiState.value is SearchUiState.NoConnection) {
                    val q = _query.value
                    if (q.isNotBlank()) performSearch(q, debounceMs = 0L)
                }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isBlank()) {
            searchJob?.cancel()
            _uiState.value = SearchUiState.Idle
            return
        }
        performSearch(newQuery)
    }

    fun retry() {
        val q = _query.value
        if (q.isNotBlank()) performSearch(q, debounceMs = 0L)
    }

    /** Zapis wyniku TMDb do Room (offline-capable po pobraniu) */
    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            val id = shelfRepository.addFromTmdb(movie)
            _addedMovieId.emit(id)
        }
    }

    /** Pusty wpis bez API — też lokalnie */
    fun addManual() {
        viewModelScope.launch {
            val id = shelfRepository.addManual("Nowy film")
            _addedMovieId.emit(id)
        }
    }

    private fun performSearch(rawQuery: String, debounceMs: Long = 400L) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (debounceMs > 0) delay(debounceMs)
            if (_query.value.trim() != rawQuery.trim()) return@launch

            _uiState.value = SearchUiState.Loading
            when (val result = movieRepository.searchMovies(rawQuery)) {
                is ApiResult.Success -> {
                    _uiState.value = if (result.data.isEmpty()) {
                        SearchUiState.Empty
                    } else {
                        SearchUiState.Success(result.data)
                    }
                }
                is ApiResult.Error -> _uiState.value = SearchUiState.Error(result.message)
                ApiResult.NoConnection -> _uiState.value = SearchUiState.NoConnection
            }
        }
    }
}
