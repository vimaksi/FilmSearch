package com.example.filmsearch.ui.poster

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.filmsearch.R
import com.example.filmsearch.domain.Creator

class PosterActivity : AppCompatActivity() {
    private val posterController = Creator.providePosterController(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.film_card)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.card)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        posterController.onCreate()
    }
}