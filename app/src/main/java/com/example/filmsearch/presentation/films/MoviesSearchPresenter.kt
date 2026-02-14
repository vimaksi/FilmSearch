package com.example.filmsearch.presentation.films

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import com.example.filmsearch.R
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.ui.films.FilmsAdapter
import com.example.filmsearch.util.Creator

class MoviesSearchPresenter(
    private val view: FilmsView,
    private val context: Context,
) {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val moviesInteractor = Creator.provideMoviesInteractor(context)
    private val films = ArrayList<Film>()
    fun onCreate() {
       // adapter.films = films
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }


    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            view.showPlaceholderMessage(true)
            view.updateFilmsList(films)
            view.changePlaceholderText(text)
            if (additionalMessage.isNotEmpty()) {
                view.showToast(text.toString())
            }
        } else {
            view.showPlaceholderMessage(false)
        }
    }

    fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            view.showPlaceholderMessage(false)
            view.showMoviesList(false)
            view.showProgressBar(true)

            moviesInteractor.searchMovies(newSearchText, object : FilmsInteractor.FilmsConsumer {
                override fun consume(foundMovies: List<Film>?, errorMessage: String?) {
                    handler.post {
                        view.showProgressBar(false)
                        if (foundMovies != null) {
                            view.updateFilmsList(films)
                            view.showMoviesList(true)
                        }
                        if (errorMessage != null) {
                            // Поменяли view на Context
                            showMessage(
                                context.getString(R.string.something_went_wrong),
                                errorMessage
                            )
                        } else if (films.isEmpty()) {
                            // И здесь поменяли view на Context
                            showMessage(context.getString(R.string.nothing_found), "")
                        } else {
                            showMessage(context.getString(R.string.nothing_found), "")
                        }
                    }
                }
            })
        }
    }
}