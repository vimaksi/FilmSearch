package com.example.filmsearch.util

import android.app.Application
import com.example.filmsearch.di.dataModule
import com.example.filmsearch.di.interactorModule
import com.example.filmsearch.di.repositoryModule
import com.example.filmsearch.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MoviesApplication)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule
            )
        }
    }
}