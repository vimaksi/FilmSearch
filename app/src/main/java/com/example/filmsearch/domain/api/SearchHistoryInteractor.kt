package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Film

interface SearchHistoryInteractor {

    fun getHistory(consumer: HistoryConsumer)
    fun saveToHistory(m: Film)

    interface HistoryConsumer {
        fun consume(searchHistory: List<Film>?)
    }
}