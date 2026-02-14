package com.example.filmsearch.domain.impl

import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.util.Resource
import java.util.concurrent.Executors

class FilmsInteractorImpl(private val repository: FilmsRepository) : FilmsInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String, consumer: FilmsInteractor.FilmsConsumer) {
        executor.execute {
            when(val resource = repository.searchMovies(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}