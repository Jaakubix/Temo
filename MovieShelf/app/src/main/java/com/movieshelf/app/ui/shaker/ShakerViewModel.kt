package com.movieshelf.app.ui.shaker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieshelf.app.data.model.ShelfMovie
import com.movieshelf.app.data.repository.ShelfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShakerViewModel(
    private val shelfRepository: ShelfRepository
) : ViewModel() {

    private val _picked = MutableStateFlow<ShelfMovie?>(null)
    val picked: StateFlow<ShelfMovie?> = _picked.asStateFlow()

    private val _emptyWatchlist = MutableStateFlow(false)
    val emptyWatchlist: StateFlow<Boolean> = _emptyWatchlist.asStateFlow()

    /** Losuje film z lokalnej listy „Do obejrzenia” (działa offline) */
    fun shake() {
        viewModelScope.launch {
            val list = shelfRepository.getWatchlist()
            if (list.isEmpty()) {
                _picked.value = null
                _emptyWatchlist.value = true
            } else {
                _emptyWatchlist.value = false
                _picked.value = list.random()
            }
        }
    }
}
