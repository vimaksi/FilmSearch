package com.example.filmsearch.presentation.name

import com.example.filmsearch.domain.models.Name

sealed interface NameState {

    object Loading : NameState

    data class Content(
        val names: List<Name>
    ) : NameState

    data class Error(
        val errorMessage: String
    ) : NameState

    data class Empty(
        val message: String
    ) : NameState
}