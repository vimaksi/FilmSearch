package com.example.filmsearch.presentation.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.MovieCast

class CastViewModel(
    private val movieId: String, private val moviesInteractor: FilmsInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<MoviesCastState>()
    fun observeState(): LiveData<MoviesCastState> = stateLiveData
    init {
        stateLiveData.postValue(MoviesCastState.Loading)
        moviesInteractor.getCast(movieId,object : FilmsInteractor.CastConsumer{
            override fun consume(movieCast: MovieCast?, errorMessage: String?) {
                if (movieCast != null){
                    stateLiveData.postValue(castToUiStateContent(movieCast))
                } else {
                    stateLiveData.postValue(MoviesCastState.Error(errorMessage ?: "Unknown error"))
                }
            }
        })
    }
    private fun castToUiStateContent(cast: MovieCast): MoviesCastState {
        // Строим список элементов RecyclerView
        val items = buildList<MoviesCastRVItem> {
            // Если есть хотя бы один режиссёр, добавим заголовок
            if (cast.directors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Directors")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один сценарист, добавим заголовок
            if (cast.writers.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Writers")
                this += cast.writers.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один актёр, добавим заголовок
            if (cast.actors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Actors")
                this += cast.actors.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один дополнительный участник, добавим заголовок
            if (cast.others.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Others")
                this += cast.others.map { MoviesCastRVItem.PersonItem(it) }
            }
        }


        return MoviesCastState.Content(
            fullTitle = cast.fullTitle,
            items = items
        )
    }
//    В коде мы добавили конвертацию объекта MovieCast в нужный объект со всеми элементами списка.
//    При конвертации объекта мы последовательно проверяем, есть ли в MovieCast информация о режиссёрах, актёрах.
//    И если есть, то добавляем заголовки для каждого раздела. Заметьте, что список элементов RecyclerView мы наполняем
//    с помощью специальной Kotlin-функции buildList, которая отдаёт нам List указанного типа и позволяет внутри лямбды модифицировать список.
}