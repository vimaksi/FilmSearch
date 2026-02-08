package com.example.filmsearch.data.dto

import com.example.filmsearch.domain.models.Film

class FilmsSearchResponse (val searchType: String,
                           val expression: String,
                           val results: List<FilmDto>) : Response()