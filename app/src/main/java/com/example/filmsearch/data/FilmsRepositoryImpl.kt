package com.example.filmsearch.data

import com.example.filmsearch.data.dto.cast.MovieCastRequest
import com.example.filmsearch.data.dto.cast.CastResponse
import com.example.filmsearch.data.dto.filmsearch.FilmSearchRequest
import com.example.filmsearch.data.dto.filmsearch.FilmsSearchResponse
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsRequest
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsResponse
import com.example.filmsearch.domain.api.FilmsRepository
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.MovieCast
import com.example.filmsearch.data.dto.cast.MovieCastConverter
import com.example.filmsearch.data.dto.name.NameSearchRequest
import com.example.filmsearch.data.dto.name.NameSearchResponse
import com.example.filmsearch.domain.models.MovieDetails
import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.List

class FilmsRepositoryImpl(
    private val networkClient: NetworkClient,   // Добавили конвертер
    private val movieCastConverter: MovieCastConverter,
) : FilmsRepository {

    override fun searchMovies(expression: String): Flow<Resource<List<Film>>> = flow {
        val response = networkClient.doRequest(FilmSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as FilmsSearchResponse) {
                    val data =
                        response.results.map {
                            Film(it.id, it.resultType, it.image, it.title, it.description)
                        }
                    emit(Resource.Success(data))
                }
            }


            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }


    override fun getMovieDetail(movieId: String): Flow<Resource<MovieDetails>> = flow {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as MovieDetailsResponse) {
                    val data =
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
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }


    override fun getCast(movieId: String): Flow<Resource<MovieCast>> = flow {
        val response = networkClient.doRequest(MovieCastRequest(movieId))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))
            200 -> {
                emit(
                    Resource.Success(
                        data = movieCastConverter.convert(response as CastResponse)
                    )
                )
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
