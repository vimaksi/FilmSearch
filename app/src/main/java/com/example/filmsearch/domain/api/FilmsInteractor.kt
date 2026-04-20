package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface FilmsInteractor {
    fun searchMovies(expression: String): Flow<Pair<List<Film>?, String?>>

    fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>>
    fun getCast(movieId: String): Flow<Pair<MovieCast?, String?>>
}