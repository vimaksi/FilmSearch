package com.example.filmsearch.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.filmsearch.data.NetworkClient
import com.example.filmsearch.data.dto.cast.MovieCastRequest
import com.example.filmsearch.data.dto.moviedetails.MovieDetailsRequest
import com.example.filmsearch.data.dto.Response
import com.example.filmsearch.data.dto.filmsearch.FilmSearchRequest
import com.example.filmsearch.data.dto.name.NameSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val imdbService: FilmApi, private val context: Context) :
    NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }

        // Добавили ещё одну проверку
        if ((dto !is FilmSearchRequest) && (dto !is MovieDetailsRequest)
            && (dto !is MovieCastRequest) && (dto !is NameSearchRequest)
        ) {
            return Response().apply { resultCode = 400 }
        }

//
//        val body = response.body()
//        return if (body != null) {
//            body.apply { resultCode = response.code() }
//        } else {
//            Response().apply { resultCode = response.code() }
//        }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is FilmSearchRequest -> {
                        imdbService.getFilms(dto.expression)
                    }

                    is MovieDetailsRequest -> {
                        imdbService.getMovieDetails(dto.movieId)
                    }

                    is NameSearchRequest -> {
                        imdbService.getName(dto.expression)
                    }

                    else -> {
                        imdbService.getCast((dto as MovieCastRequest).movieId)
                    }
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
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
