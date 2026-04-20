package com.example.filmsearch.presentation.poster

import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieDetails

sealed interface AboutState {

    data class Content(
        val movieDetails: MovieDetails
    ) : AboutState

    data class Error(
        val errorMessage: String
    ) : AboutState
}