package com.example.filmsearch.domain.models

import com.example.filmsearch.domain.models.MovieCastPerson

data class MovieCast(
    val imdbId: String,
    val fullTitle: String,
    val directors: List<MovieCastPerson>,
    val writers: List<MovieCastPerson>,
    val actors: List<MovieCastPerson>,
    val others: List<MovieCastPerson>,
)