package com.movieshelf.app.ui.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieshelf.app.data.model.MovieStatus
import com.movieshelf.app.data.model.ShelfMovie
import com.movieshelf.app.data.repository.ShelfRepository
import com.movieshelf.app.network.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Globalny stan półki — obserwuje Room Flow.
 * Wszystkie ekrany widzą te same dane po Create/Update/Delete.
 */
class ShelfViewModel(
    shelfRepository: ShelfRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    val movies: StateFlow<List<ShelfMovie>> = shelfRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val isOnline: StateFlow<Boolean> = networkMonitor.connectivityFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), networkMonitor.isOnline())

    fun filter(tabIndex: Int, all: List<ShelfMovie>): List<ShelfMovie> = when (tabIndex) {
        1 -> all.filter { it.status == MovieStatus.WATCHED }
        2 -> all.filter { it.isFavorite }
        else -> all.filter { it.status == MovieStatus.WATCHLIST }
    }
}
