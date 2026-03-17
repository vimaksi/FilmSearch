package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.domain.models.MovieDetails

interface FilmsInteractor {
    fun searchMovies(expression: String, consumer: FilmsConsumer)

    interface FilmsConsumer {
        fun consume(foundMovies: List<Film>?, errorMessage: String?)
    }
    fun getMovieDetails(movieId: String, consumer: MovieDetailsConsumer)
    interface MovieDetailsConsumer {
        fun consume(foundMovieDetails: MovieDetails?, errorMessage: String?)
    }
    fun getCast(movieId: String,consumer: CastConsumer)
    interface CastConsumer{
        fun consume(foundCast: MovieCast?,errorMessage: String?)
    }
}