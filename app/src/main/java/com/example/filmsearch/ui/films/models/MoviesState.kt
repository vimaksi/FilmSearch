package com.example.filmsearch.ui.films.models

import com.example.filmsearch.domain.models.Film

sealed interface MoviesState {

    object Loading : MoviesState

    data class Content(
        val movies: List<Film>
    ) : MoviesState

    data class Error(
        val errorMessage: String
    ) : MoviesState

    data class Empty(
        val message: String
    ) : MoviesState

}