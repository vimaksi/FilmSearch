package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.domain.models.MovieDetails
import com.example.filmsearch.util.Resource

interface FilmsRepository {
    fun searchMovies(expression: String): Resource<List<Film>>
    fun getMovieDetail(movieId: String): Resource<MovieDetails>
    fun getCast(movieId:String) : Resource<MovieCast>
}