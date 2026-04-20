package com.example.filmsearch.data.dto.name

import com.example.filmsearch.data.dto.Response

class NameSearchResponse (val searchType: String,
                          val expression: String,
                          val results: List<NameDto>) : Response()