package com.example.filmsearch.presentation

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.filmsearch.R

class PosterController(private val activity: Activity) {
     fun onCreate() {
        val itemView = activity.findViewById<ImageView>(R.id.image)
        val image = activity.intent.getStringExtra("poster")

        Glide.with(activity.applicationContext)
            .load(image)
            .centerCrop()
            .into(itemView)
    }
}
