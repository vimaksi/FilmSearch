package com.example.filmsearch.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.MovieDetails
import kotlinx.coroutines.launch

class AboutViewModel(
    private val movieId: String,
    private val moviesInteractor: FilmsInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<AboutState>()
    fun observeState(): LiveData<AboutState> = stateLiveData

    init {
        viewModelScope.launch {
            moviesInteractor.getMovieDetails(movieId)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(movieDetails: MovieDetails?, errorMessage: String?) {
        if (movieDetails != null) {
            stateLiveData.postValue(AboutState.Content(movieDetails))
        } else {
            stateLiveData.postValue(AboutState.Error(errorMessage ?: "Unknown error"))
        }
    }
}