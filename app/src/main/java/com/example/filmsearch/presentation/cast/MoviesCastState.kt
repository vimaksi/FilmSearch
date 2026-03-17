package com.example.filmsearch.presentation.cast

import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.ui.core.RVItem

sealed interface MoviesCastState {

    data class Content(
        val fullTitle: String,
        val items: List<RVItem>,
    ) : MoviesCastState

    data class Error(
        val errorMessage: String
    ) : MoviesCastState

    object Loading: MoviesCastState
}