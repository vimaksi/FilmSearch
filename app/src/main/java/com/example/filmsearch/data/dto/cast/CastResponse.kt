package com.example.filmsearch.data.dto.cast

import com.example.filmsearch.data.dto.Response

class CastResponse(
    val actors: List<Actor>,
    val directors: Directors,
    val errorMessage: String,
    val fullTitle: String,
    val imDbId: String,
    val others: List<Other>,
    val title: String,
    val type: String,
    val writers: Writers,
    val year: String
) : Response()



