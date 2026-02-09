package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film

interface FilmsRepository {
    fun searchMovies(expression: String): Result<List<Film>>
}