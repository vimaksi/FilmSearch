package com.example.filmsearch.di

import com.example.filmsearch.data.FilmsRepositoryImpl
import com.example.filmsearch.data.SearchHistoryRepositoryImpl
import com.example.filmsearch.data.dto.cast.MovieCastConverter
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.api.SearchHistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { MovieCastConverter() }

    single<FilmsRepository> {
        FilmsRepositoryImpl(get(),get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
}
