package com.example.filmsearch.domain.db

import com.example.filmsearch.domain.models.Film
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun historyMovies(): Flow<List<Film>>
}