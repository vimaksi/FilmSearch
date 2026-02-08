package com.example.filmsearch.data

import com.example.filmsearch.data.dto.Response


interface NetworkClient {
    fun doRequest(dto: Any): Response

}