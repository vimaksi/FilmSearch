package com.example.filmsearch.domain.impl

import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.api.FilmsRepository
import java.util.concurrent.Executors

class FilmsInteractorImpl(private val repository: FilmsRepository) : FilmsInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String, consumer: FilmsInteractor.MoviesConsumer) {
        executor.execute {
            consumer.consume(repository.searchMovies(expression))
        }
    }
}