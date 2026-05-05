package com.example.filmsearch.domain.impl

import com.example.filmsearch.domain.db.HistoryInteractor
import com.example.filmsearch.domain.db.HistoryRepository
import com.example.filmsearch.domain.models.Film
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun historyMovies(): Flow<List<Film>> {
        return historyRepository.historyMovies()
    }
}