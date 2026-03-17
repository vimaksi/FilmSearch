package com.example.filmsearch.data

import com.example.filmsearch.data.dto.cast.CastRequest
import com.example.filmsearch.data.dto.cast.CastResponse
import com.example.filmsearch.data.dto.filmsearch.FilmSearchRequest
import com.example.filmsearch.data.dto.filmsearch.FilmsSearchResponse
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsRequest
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsResponse
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.data.dto.cast.MovieCastConverter
import com.example.filmsearch.domain.models.MovieDetails
import com.example.filmsearch.util.Resource
import kotlin.collections.List

class FilmsRepositoryImpl(
    private val networkClient: NetworkClient,   // Добавили конвертер
    private val movieCastConverter: MovieCastConverter,
) : FilmsRepository {

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

    override fun getMovieDetail(movieId: String): Resource<MovieDetails> {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                with(response as MovieDetailsResponse) {
                    Resource.Success(
                        MovieDetails(
                            id = id,
                            title = title,
                            imDbRating = imDbRating,
                            year = year,
                            countries = countries,
                            genres = genres,
                            directors = directors,
                            writers = writers,
                            stars = stars,
                            plot = plot,
                        )
                    )
                }
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getCast(movieId: String): Resource<MovieCast> {
        val response = networkClient.doRequest(CastRequest(movieId))
        return when (response.resultCode) {
            -1 -> Resource.Error("Проверьте подключение к интернету")
            200 -> {                // используем конвертер вместо
                // прямой конвертации
                Resource.Success(
                    data = movieCastConverter.convert(response as CastResponse)
                )
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}
