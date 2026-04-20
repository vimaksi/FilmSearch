package com.example.filmsearch.domain.impl

import com.example.filmsearch.domain.api.SearchHistoryInteractor
import com.example.filmsearch.domain.api.SearchHistoryRepository
import com.example.filmsearch.domain.models.Film

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun getHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        consumer.consume(repository.getHistory().data)
    }

    override fun saveToHistory(m: Film) {
        repository.saveToHistory(m)
    }
}