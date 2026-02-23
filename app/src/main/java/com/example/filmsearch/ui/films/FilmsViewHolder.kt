package com.example.filmsearch.ui.films

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmsearch.R
import com.example.filmsearch.databinding.ActivityMainBinding
import com.example.filmsearch.databinding.FilmItemBinding
import com.example.filmsearch.domain.models.Film

class FilmsViewHolder(private val binding: FilmItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Film) {
        binding.apply {
            filmName.text = model.title
            filmDescription.text = model.description
        }
        Glide.with(itemView)
            .load(model.image)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .into(binding.filmAlbum)
    }

    companion object {
        fun from(parent: ViewGroup): FilmsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FilmItemBinding.inflate(inflater, parent, false)
            return FilmsViewHolder(binding)
        }
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}