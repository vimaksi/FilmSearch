package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.util.Resource

interface FilmsInteractor {
    fun searchMovies(expression: String, consumer: FilmsConsumer)

    interface FilmsConsumer {
        fun consume(foundMovies: List<Film>?, errorMessage: String?)
    }
}