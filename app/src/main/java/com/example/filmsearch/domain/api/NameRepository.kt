package com.example.filmsearch.domain.api

import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.util.Resource
import kotlinx.coroutines.flow.Flow

interface NameRepository {
    fun getName(expression: String): Flow<Resource<List<Name>>>
}