package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.domain.models.MovieDetails
import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.util.Resource
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {
    fun searchMovies(expression: String): Flow<Resource<List<Film>>>
    fun getMovieDetail(movieId: String): Flow<Resource<MovieDetails>>
    fun getCast(movieId:String) : Flow<Resource<MovieCast>>
}