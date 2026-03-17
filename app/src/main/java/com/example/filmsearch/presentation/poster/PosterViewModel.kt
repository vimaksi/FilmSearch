package com.example.filmsearch.presentation.poster

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.initializer

class PosterViewModel(private val imageUrl: String): ViewModel() {
    private val url = MutableLiveData(imageUrl)
    fun observeUrl(): LiveData<String> = url
}