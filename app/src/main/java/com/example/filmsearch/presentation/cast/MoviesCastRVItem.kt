package com.example.filmsearch.presentation.cast

import com.example.filmsearch.domain.models.MovieCastPerson
import com.example.filmsearch.ui.core.RVItem


sealed interface MoviesCastRVItem: RVItem {

    data class HeaderItem(
        val headerText: String,
    ) : MoviesCastRVItem

    data class PersonItem(
        val data: MovieCastPerson,
    ) : MoviesCastRVItem

}