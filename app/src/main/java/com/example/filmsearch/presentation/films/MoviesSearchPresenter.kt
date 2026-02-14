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
            view.render(
                MoviesState.Loading
            )

            moviesInteractor.searchMovies(newSearchText, object : FilmsInteractor.FilmsConsumer {
                override fun consume(foundMovies: List<Film>?, errorMessage: String?) {
                    handler.post {
                        if (foundMovies != null) {
                            films.clear()
                            films.addAll(foundMovies)
                        }

                        when {
                            errorMessage != null -> {
                                view.render(
                                    MoviesState.Error(
                                        errorMessage = context.getString(R.string.something_went_wrong),
                                    )
                                )
                                view.showToast(errorMessage)
                            }

                            films.isEmpty() -> {
                                view.render(
                                    MoviesState.Empty(
                                        message = context.getString(R.string.nothing_found),
                                    )
                                )
                            }

                            else -> {
                                view.render(
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
//                    handler.post {
//                        if (foundMovies != null) {
//                            films.clear()
//                            films.addAll(foundMovies)
//                        }
//
//                        when {
//                            errorMessage != null -> {
//                                view.render(
//                                    FilmsState(
//                                        movies = emptyList(),
//                                        isLoading = false,
//                                        errorMessage = context.getString(R.string.something_went_wrong),
//                                    )
//                                )
//                                view.showToast(errorMessage)
//                            }
//
//                            films.isEmpty() -> {
//                                view.render(
//                                    FilmsState(
//                                        movies = emptyList(),
//                                        isLoading = false,
//                                        errorMessage = context.getString(R.string.nothing_found),
//                                    )
//                                )
//                            }
//
//                            else -> {
//                                view.render(
//                                    FilmsState(
//                                        movies = films,
//                                        isLoading = false,
//                                        errorMessage = null,
//                                    )
//                                )
//                            }
//                        }
//
//                    }
//                }
//            })
    }
}
