package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.util.Resource

interface SearchHistoryRepository {
    fun saveToHistory(m: Film)
    fun getHistory(): Resource<List<Film>>
}