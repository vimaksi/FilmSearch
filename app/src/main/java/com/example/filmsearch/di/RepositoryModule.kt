package com.example.filmsearch.di

import com.example.filmsearch.data.FilmsRepositoryImpl
import com.example.filmsearch.data.HistoryRepositoryImpl
import com.example.filmsearch.data.NameRepositoryImpl
import com.example.filmsearch.data.SearchHistoryRepositoryImpl
import com.example.filmsearch.data.converters.MovieDbConvertor
import com.example.filmsearch.data.dto.cast.MovieCastConverter
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.api.NameRepository
import com.example.filmsearch.domain.api.SearchHistoryRepository
import com.example.filmsearch.domain.db.HistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { MovieCastConverter() }

    single<FilmsRepository> {
        FilmsRepositoryImpl(get(),get(),get(),get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<NameRepository>{
        NameRepositoryImpl(get())
    }
    factory{
        MovieDbConvertor()
    }
    single<HistoryRepository>{
        HistoryRepositoryImpl(get(), get())
    }
}
