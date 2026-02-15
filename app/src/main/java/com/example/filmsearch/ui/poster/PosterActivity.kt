package com.example.filmsearch.ui.poster

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.filmsearch.R
import com.example.filmsearch.presentation.PosterPresenter
import com.example.filmsearch.presentation.poster.PosterView
import com.example.filmsearch.util.Creator

class PosterActivity : AppCompatActivity(), PosterView {
    private lateinit var posterPresenter: PosterPresenter
    private lateinit var itemView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.film_card)
        val imageUrl = intent.extras?.getString("poster", "") ?: ""
        posterPresenter = Creator.providePosterController(this, imageUrl)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.card)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        itemView = findViewById(R.id.image)
        posterPresenter.onCreate()
    }

    override fun showPoster(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(itemView)
    }
}