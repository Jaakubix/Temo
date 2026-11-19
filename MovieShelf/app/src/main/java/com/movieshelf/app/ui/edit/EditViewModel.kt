package com.movieshelf.app.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieshelf.app.data.model.MovieStatus
import com.movieshelf.app.data.model.ShelfMovie
import com.movieshelf.app.data.repository.ShelfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditViewModel(
    private val movieId: Long,
    private val shelfRepository: ShelfRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<ShelfMovie?>(null)
    val movie: StateFlow<ShelfMovie?> = _movie.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved.asStateFlow()

    private val _deleted = MutableStateFlow(false)
    val deleted: StateFlow<Boolean> = _deleted.asStateFlow()

    init {
        viewModelScope.launch {
            _movie.value = shelfRepository.getById(movieId)
        }
    }

    /**
     * @param statusIndex 0 = Do obejrzenia, 1 = Obejrzany, 2 = Ulubiony
     */
    fun save(
        statusIndex: Int,
        rating: Float,
        note: String,
        locationLabel: String,
        lat: Double?,
        lng: Double?
    ) {
        val current = _movie.value ?: return
        val updated = when (statusIndex) {
            1 -> current.copy(
                status = MovieStatus.WATCHED,
                isFavorite = false
            )
            2 -> current.copy(isFavorite = true)
            else -> current.copy(
                status = MovieStatus.WATCHLIST,
                isFavorite = false
            )
        }.copy(
            rating = rating,
            note = note.ifBlank { null },
            locationLabel = locationLabel.ifBlank { null },
            locationLat = lat,
            locationLng = lng
        )

        viewModelScope.launch {
            shelfRepository.update(updated)
            _saved.value = true
        }
    }

    fun delete() {
        viewModelScope.launch {
            shelfRepository.delete(movieId)
            _deleted.value = true
        }
    }
}
