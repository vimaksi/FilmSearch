package com.example.filmsearch.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.filmsearch.data.NetworkClient
import com.example.filmsearch.data.dto.cast.CastRequest
import com.example.filmsearch.data.dto.filmsearch.FilmSearchRequest
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsRequest
import com.example.filmsearch.data.dto.Response

class RetrofitNetworkClient(private val imdbService: FilmApi, private val context: Context) :
    NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        if ((dto !is FilmSearchRequest) && (dto !is MovieDetailsRequest) && (dto !is CastRequest)) {
            return Response().apply { resultCode = 400 }
        }
        // Добавился ещё один if
        val response = when (dto) {
            is FilmSearchRequest -> {
                imdbService.getFilms(dto.expression).execute()
            }

            is MovieDetailsRequest -> {
                imdbService.getMovieDetails(dto.movieId).execute()
            }

            else -> {
                imdbService.getCast((dto as CastRequest).movieId).execute()
            }
        }

        val body = response.body()
        return if (body != null) {
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}

