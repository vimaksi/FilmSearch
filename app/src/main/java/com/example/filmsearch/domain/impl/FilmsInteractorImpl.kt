package com.example.filmsearch.domain.impl

import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.domain.models.MovieDetails
import com.example.filmsearch.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilmsInteractorImpl(private val repository: FilmsRepository) : FilmsInteractor {


    override fun searchMovies(expression: String): Flow<Pair<List<Film>?, String?>> {
        return repository.searchMovies(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getMovieDetails(
        movieId: String
    ): Flow<Pair<MovieDetails?, String?>> {
        return repository.getMovieDetail(movieId).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }


    override fun getCast(
        movieId: String
    ): Flow<Pair<MovieCast?, String?>> {
        return repository.getCast(movieId).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}
