package com.example.filmsearch.presentation.films


import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.presentation.SingleLiveEvent
import com.example.filmsearch.util.debounce
import kotlinx.coroutines.launch


class MoviesViewModel(private val moviesInteractor: FilmsInteractor) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData
    private val showToast = SingleLiveEvent<String?>()
    private val movieSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchRequest(changedText)
        }

    fun observeShowToast(): LiveData<String?> = showToast
    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            movieSearchDebounce(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                MoviesState.Loading
            )
            viewModelScope.launch {
                moviesInteractor.searchMovies(newSearchText)
                    .collect { pair -> processResult(pair.first, pair.second) }
            }
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

    private fun processResult(foundMovies: List<Film>?, errorMessage: String?) {
        // Готовим список найденных фильмов для передачи в конструктор MoviesState
        val movies = mutableListOf<Film>()
        if (foundMovies != null) {
            movies.addAll(foundMovies)
        }

        when {
            errorMessage != null -> {
                renderState(
                    MoviesState.Error(
                        ""//errorMessage = context.getString(R.string.something_went_wrong),
                    )
                )
                showToast.postValue(errorMessage)
            }

            movies.isEmpty() -> {
                renderState(
                    MoviesState.Empty(
                        ""// message = context.getString(R.string.nothing_found),
                    )
                )
            }

            else -> {
                renderState(
                    MoviesState.Content(
                        movies = movies,
                    )
                )
            }
        }
    }
}