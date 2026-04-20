package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Name
import kotlinx.coroutines.flow.Flow

interface NameInteractor {
    fun getName(expression: String): Flow<Pair<List<Name>?, String?>>
}