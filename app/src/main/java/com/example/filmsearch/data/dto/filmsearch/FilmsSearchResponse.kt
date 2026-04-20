package com.example.filmsearch.data.dto.filmsearch

import com.example.filmsearch.data.dto.Response

class FilmsSearchResponse (val searchType: String,
                           val expression: String,
                           val results: List<FilmDto>) : Response()