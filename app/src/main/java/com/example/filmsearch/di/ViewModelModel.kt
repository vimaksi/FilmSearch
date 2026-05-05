package com.example.filmsearch.di

import com.example.filmsearch.presentation.cast.CastViewModel
import com.example.filmsearch.presentation.films.MoviesViewModel
import com.example.filmsearch.presentation.history.HistoryViewModel
import com.example.filmsearch.presentation.name.NameViewModel
import com.example.filmsearch.presentation.poster.AboutViewModel
import com.example.filmsearch.presentation.poster.PosterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MoviesViewModel(get())
    }
    viewModel { (posterUrl: String) ->
        PosterViewModel(posterUrl)
    }
    viewModel { (movieId: String) ->
        AboutViewModel(movieId, get())
    }
    viewModel { (movieId: String) ->
        CastViewModel(movieId, get())
    }
    viewModel { NameViewModel(androidContext(), get()) }
    viewModel {
        HistoryViewModel(androidContext(), get())
    }
}