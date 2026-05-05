package com.example.filmsearch.data.converters

import com.example.filmsearch.data.db.entity.MovieEntity
import com.example.filmsearch.domain.models.Film


class MovieDbConvertor {
    fun map(movie: Film): MovieEntity {
        return MovieEntity(
            movie.id, movie.resultType, movie.image, movie.title, movie.description
        )
    }

    fun map(movie: MovieEntity): Film {
        return Film(
            movie.id, movie.resultType, movie.image, movie.title, movie.description
        )
    }
}