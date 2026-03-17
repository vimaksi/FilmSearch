package com.example.filmsearch.data.network

import com.example.filmsearch.data.dto.cast.CastResponse
import com.example.filmsearch.data.dto.filmsearch.FilmsSearchResponse
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FilmApi {

    @GET("/en/API/SearchMovie/k_zcuw1ytf/{expression}")
    fun getFilms(@Path("expression") expression: String): Call<FilmsSearchResponse>

    @GET("/en/API/Title/k_zcuw1ytf/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: String): Call<MovieDetailsResponse>

    @GET("en/API/FullCast/k_zcuw1ytf/{movie_id}")
    fun getCast(@Path("movie_id") movieId: String): Call<CastResponse>
}