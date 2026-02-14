package com.example.filmsearch.presentation.films

import com.example.filmsearch.ui.films.models.MoviesState

interface FilmsView {
    fun render(state: MoviesState)

    // Методы одноразовых событий

    fun showToast(additionalMessage: String)
}