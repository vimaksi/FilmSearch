package com.example.filmsearch.domain

import com.example.filmsearch.data.MoviesRepositoryImpl
import com.example.filmsearch.data.network.RetrofitNetworkClient
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.impl.FilmsInteractorImpl

object Creator {
    private fun getMoviesRepository(): FilmsRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): FilmsInteractor {
        return FilmsInteractorImpl(getMoviesRepository())
    }
}