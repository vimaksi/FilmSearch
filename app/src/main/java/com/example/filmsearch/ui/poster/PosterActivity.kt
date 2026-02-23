package com.example.filmsearch.ui.poster

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.filmsearch.R
import com.example.filmsearch.presentation.films.MoviesViewModel
import com.example.filmsearch.presentation.poster.PosterViewModel
import com.example.filmsearch.util.Creator

class PosterActivity: AppCompatActivity()  {
    private lateinit var itemView: ImageView
    private var viewModel: PosterViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.film_card)
        val imageUrl = intent.extras?.getString("poster", "") ?: ""

        itemView = findViewById(R.id.image)
        viewModel = ViewModelProvider(this, PosterViewModel.getFactory(imageUrl))
            .get(PosterViewModel::class.java)
        viewModel?.observedUrl()?.observe(this){
            showPoster(it)
        }
    }

    fun showPoster(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(itemView)
    }
}