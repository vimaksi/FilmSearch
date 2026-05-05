package com.example.filmsearch.presentation.history

import com.example.filmsearch.domain.models.Film

sealed interface HistoryState {
    object Loading : HistoryState
    data class Content(val movies: List<Film>) : HistoryState
    data class Empty(val message: String) : HistoryState
}
