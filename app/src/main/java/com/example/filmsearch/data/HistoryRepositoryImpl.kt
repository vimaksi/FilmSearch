package com.example.filmsearch.data

import com.example.filmsearch.data.converters.MovieDbConvertor
import com.example.filmsearch.data.db.AppDatabase
import com.example.filmsearch.data.db.entity.MovieEntity
import com.example.filmsearch.domain.db.HistoryRepository
import com.example.filmsearch.domain.models.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConvertor,
) : HistoryRepository {
    override fun historyMovies(): Flow<List<Film>> = flow {
        val movies = appDatabase.movieDao().getAllMovies()
        emit(convertFromMovieEntity(movies))
    }

    private fun convertFromMovieEntity(movies: List<MovieEntity>): List<Film> {
        return movies.map { movie -> movieDbConvertor.map(movie) }
    }
}