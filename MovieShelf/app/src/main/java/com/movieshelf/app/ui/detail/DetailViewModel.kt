package com.movieshelf.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieshelf.app.data.model.ShelfMovie
import com.movieshelf.app.data.repository.ShelfRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val movieId: Long,
    private val shelfRepository: ShelfRepository
) : ViewModel() {

    val movie: StateFlow<ShelfMovie?> = shelfRepository.observeById(movieId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun markWatched() {
        viewModelScope.launch { shelfRepository.markWatched(movieId) }
    }

    fun toggleFavorite() {
        viewModelScope.launch { shelfRepository.toggleFavorite(movieId) }
    }
}
