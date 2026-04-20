package com.example.filmsearch.domain.impl

import com.example.filmsearch.domain.api.NameInteractor
import com.example.filmsearch.domain.api.NameRepository
import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NameInteractorImpl(private val repository: NameRepository) : NameInteractor {
    override fun getName(expression: String): Flow<Pair<List<Name>?, String?>> {
        return repository.getName(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}