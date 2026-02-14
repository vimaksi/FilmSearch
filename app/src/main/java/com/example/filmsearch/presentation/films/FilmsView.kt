package com.example.filmsearch.presentation.films

import android.content.Context
import com.example.filmsearch.domain.models.Film

interface FilmsView {

    fun showPlaceholderMessage(isVisible: Boolean)

    fun showMoviesList(isVisible: Boolean)

    fun showProgressBar(isVisible: Boolean)

    fun changePlaceholderText(newPlaceholderText: String)
    fun updateFilmsList(newFilmsList: List<Film>)
    fun showToast(text: String)
}