package com.example.filmsearch.di

import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.api.NameInteractor
import com.example.filmsearch.domain.api.SearchHistoryInteractor
import com.example.filmsearch.domain.impl.FilmsInteractorImpl
import com.example.filmsearch.domain.impl.NameInteractorImpl
import com.example.filmsearch.domain.impl.SearchHistoryInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<FilmsInteractor> {
        FilmsInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<NameInteractor>{
        NameInteractorImpl(get())
    }
}