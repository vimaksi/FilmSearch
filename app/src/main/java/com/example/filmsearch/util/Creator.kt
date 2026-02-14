package com.example.filmsearch.util

import android.content.Context
import com.example.filmsearch.data.MoviesRepositoryImpl
import com.example.filmsearch.data.network.RetrofitNetworkClient
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.impl.FilmsInteractorImpl
import com.example.filmsearch.presentation.films.MoviesSearchPresenter
import com.example.filmsearch.presentation.PosterPresenter
import com.example.filmsearch.presentation.films.FilmsView
import com.example.filmsearch.presentation.poster.PosterView
import com.example.filmsearch.ui.poster.PosterActivity

object Creator {
    private fun getMoviesRepository(context: Context): FilmsRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }
    fun provideMoviesInteractor(context: Context): FilmsInteractor {
        return FilmsInteractorImpl(getMoviesRepository(context))
    }
    fun provideMoviesSearchPresenter(
        moviesView: FilmsView,
        context: Context
    ): MoviesSearchPresenter {
        return MoviesSearchPresenter(
            view = moviesView,
            context = context,
        )
    }
    fun providePosterController(view: PosterView,imageUrl: String): PosterPresenter {
        return PosterPresenter(view, imageUrl)
    }
}