package com.example.filmsearch.util

import android.app.Application
import com.example.filmsearch.presentation.films.MoviesSearchPresenter

class MoviesApplication : Application() {

    var moviesSearchPresenter: MoviesSearchPresenter? = null

}