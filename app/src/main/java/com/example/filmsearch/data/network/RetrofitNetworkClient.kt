package com.example.filmsearch.data.network

import com.example.filmsearch.data.NetworkClient
import com.example.filmsearch.data.dto.FilmSearchRequest
import com.example.filmsearch.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val imdbBaseUrl = "https://tv-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(FilmApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is FilmSearchRequest) {
            val resp = imdbService.getFilms(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}

