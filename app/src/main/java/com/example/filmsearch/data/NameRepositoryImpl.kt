package com.example.filmsearch.data

import com.example.filmsearch.data.dto.name.NameSearchRequest
import com.example.filmsearch.data.dto.name.NameSearchResponse
import com.example.filmsearch.domain.api.NameRepository
import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NameRepositoryImpl(private val networkClient: NetworkClient) : NameRepository {
    override fun getName(expression: String): Flow<Resource<List<Name>>> = flow {
        val response = networkClient.doRequest(NameSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as NameSearchResponse) {
                    val data = response.results.map{
                        Name(
                            id = it.id,
                            name = it.title,
                            description = it.description,
                            image = it.image
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}