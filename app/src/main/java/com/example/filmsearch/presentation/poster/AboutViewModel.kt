package com.example.filmsearch.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.MovieDetails

class AboutViewModel(private val movieId: String,
                     private val moviesInteractor: FilmsInteractor, ) : ViewModel() {
    private val stateLiveData = MutableLiveData<AboutState>()
    fun observeState(): LiveData<AboutState> = stateLiveData
    init {
        moviesInteractor.getMovieDetails(movieId, object : FilmsInteractor.MovieDetailsConsumer {

            override fun consume(movieDetails: MovieDetails?, errorMessage: String?) {
                if (movieDetails != null) {
                    stateLiveData.postValue(AboutState.Content(movieDetails))
                } else {
                    stateLiveData.postValue(AboutState.Error(errorMessage ?: "Unknown error"))
                }
            }
        })
    }
}