package com.example.filmsearch.domain

import android.app.Activity
import android.content.Context
import com.example.filmsearch.data.MoviesRepositoryImpl
import com.example.filmsearch.data.network.RetrofitNetworkClient
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.impl.FilmsInteractorImpl
import com.example.filmsearch.presentation.MoviesSearchController
import com.example.filmsearch.presentation.PosterController
import com.example.filmsearch.ui.films.FilmsAdapter
import com.example.filmsearch.ui.poster.PosterActivity

object Creator {
    private fun getMoviesRepository(context: Context): FilmsRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }
    fun provideMoviesInteractor(context: Context): FilmsInteractor {
        return FilmsInteractorImpl(getMoviesRepository(context))
    }
    fun provideMoviesSearchController(activity: Activity, adapter: FilmsAdapter): MoviesSearchController {
        return MoviesSearchController(adapter,activity)
    }
    fun providePosterController(activity: PosterActivity): PosterController{
        return PosterController(activity)
    }
}