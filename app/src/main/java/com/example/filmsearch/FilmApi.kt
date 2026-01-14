package com.example.filmsearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FilmApi {
    @GET("/en/API/SearchMovie/k_zcuw1ytf/{expression}")
    fun getFilms(@Path("expression") expression: String): Call<FilmsResponse>
}