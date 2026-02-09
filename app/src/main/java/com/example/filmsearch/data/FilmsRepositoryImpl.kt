package com.example.filmsearch.data

import com.example.filmsearch.data.dto.FilmSearchRequest
import com.example.filmsearch.data.dto.FilmsSearchResponse
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.models.Film

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : FilmsRepository {

    override fun searchMovies(expression: String): Result<List<Film>> {
        return try {
            val response = networkClient.doRequest(FilmSearchRequest(expression))
            if (response.resultCode == 200) {
                return Result.success((response as FilmsSearchResponse).results.map {
                    Film(it.id, it.resultType, it.image, it.title, it.description)
                })
            } else {
                Result.failure(IllegalArgumentException(""))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
