package com.example.filmsearch.data

import com.example.filmsearch.data.dto.FilmSearchRequest
import com.example.filmsearch.data.dto.FilmsSearchResponse
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.util.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : FilmsRepository {

    override fun searchMovies(expression: String): Resource<List<Film>> {
        val response = networkClient.doRequest(FilmSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as FilmsSearchResponse).results.map {
                    Film(it.id, it.resultType, it.image, it.title, it.description)
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}
