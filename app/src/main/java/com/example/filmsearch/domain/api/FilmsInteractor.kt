package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film

interface FilmsInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Film>)
    }
}