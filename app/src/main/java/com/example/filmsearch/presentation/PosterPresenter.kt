package com.example.filmsearch.presentation

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.filmsearch.R
import com.example.filmsearch.presentation.poster.PosterView

class PosterPresenter(
    private val view: PosterView,
    private val imageUrl: String,
) {
    fun onCreate() {
        view.showPoster(imageUrl)
    }
}
