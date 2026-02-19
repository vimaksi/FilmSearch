package com.example.filmsearch.presentation.films

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.example.filmsearch.R
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.ui.films.models.MoviesState
import com.example.filmsearch.util.Creator
import moxy.MvpPresenter

class MoviesSearchPresenter(
    private val context: Context,
) : MvpPresenter<FilmsView>(){
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())
    private val moviesInteractor = Creator.provideMoviesInteractor(context)
    private val films = ArrayList<Film>()
    private var latestSearchText: String? = null

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }
    private fun renderState(state: MoviesState) {
        viewState.render(state)
    }
    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : FilmsInteractor.FilmsConsumer {
                override fun consume(foundMovies: List<Film>?, errorMessage: String?) {
                    handler.post {
                        if (foundMovies != null) {
                            films.clear()
                            films.addAll(foundMovies)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    MoviesState.Error(
                                        errorMessage = context.getString(R.string.something_went_wrong),
                                    )
                                )
                                viewState?.showToast(errorMessage)
                            }

                            films.isEmpty() -> {
                                renderState(
                                    MoviesState.Empty(
                                        errorMessage = context.getString(R.string.something_went_wrong),
                                    )
                                )
                                viewState?.showToast(errorMessage)
                            }


                            else -> {
                                renderState(
                                    MoviesState.Content(
                                        movies = films,
                                    )
                                )
                            }
                        }

                    }
                }
            })
        }
    }
}
