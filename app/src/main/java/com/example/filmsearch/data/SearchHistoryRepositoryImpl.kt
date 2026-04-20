package com.example.filmsearch.data

import com.example.filmsearch.domain.api.SearchHistoryRepository
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.util.Resource

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Film>>): SearchHistoryRepository {

    override fun saveToHistory(m: Film) {
        val movies = storage.getData() ?: arrayListOf()
        movies.add(m)
        storage.storeData(movies)
    }

    override fun getHistory(): Resource<List<Film>> {
        val movies = storage.getData() ?: listOf()
        return Resource.Success(movies)
    }
}