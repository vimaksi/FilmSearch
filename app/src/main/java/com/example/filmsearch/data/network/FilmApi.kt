package com.example.filmsearch.data.network

import com.example.filmsearch.data.dto.cast.CastResponse
import com.example.filmsearch.data.dto.filmsearch.FilmsSearchResponse
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsResponse
import com.example.filmsearch.data.dto.name.NameSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FilmApi {

    @GET("/en/API/SearchMovie/k_zcuw1ytf/{expression}")
    suspend fun getFilms(@Path("expression") expression: String): FilmsSearchResponse

    @GET("/en/API/Title/k_zcuw1ytf/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String): MovieDetailsResponse

    @GET("/en/API/FullCast/k_zcuw1ytf/{movie_id}")
    suspend fun getCast(@Path("movie_id") movieId: String): CastResponse

    @GET("/en/API/SearchName/k_zcuw1ytf/{expression}")
    suspend fun getName(@Path("expression") expression: String): NameSearchResponse
}