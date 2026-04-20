package com.example.filmsearch.presentation.name

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmsearch.R
import com.example.filmsearch.domain.api.NameInteractor
import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.presentation.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NameViewModel(private val context: Context, private val nameInteractor: NameInteractor) :
    ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private val stateLiveData = MutableLiveData<NameState>()
    fun observeState(): LiveData<NameState> = stateLiveData
    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast
    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                NameState.Loading
            )
            viewModelScope.launch {
                nameInteractor
                    .getName(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }

            }
        }
    }

    private fun renderState(state: NameState) {
        stateLiveData.postValue(state)
    }

    private fun processResult(
        foundName: List<Name>?, errorMessage: String?
    ) {
        // Готовим список найденных для передачи в конструктор NameState
        val names = mutableListOf<Name>()
        if (foundName != null) {
            names.addAll(foundName)
        }

        when {
            errorMessage != null -> {
                renderState(
                    NameState.Error(
                        context.getString(
                            R.string.something_went_wrong
                        ),
                    )
                )
                showToast.postValue(errorMessage)
            }

            names.isEmpty() -> {
                renderState(
                    NameState.Empty(
                        context.getString(R.string.nothing_found),
                    )
                )
            }

            else -> {
                renderState(
                    NameState.Content(
                        names = names,
                    )
                )
            }
        }

    }
}