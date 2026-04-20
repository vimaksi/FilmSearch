package com.example.filmsearch.ui.films

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.ui.films.FilmsViewHolder
import com.example.filmsearch.domain.models.Film

class FilmsAdapter(val clickListener: FilmClickListener): RecyclerView.Adapter<FilmsViewHolder>() {
    var films = ArrayList<Film>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder = FilmsViewHolder.from(parent)
    override fun onBindViewHolder(
        holder: FilmsViewHolder,
        position: Int
    ) {
        holder.bind(films.get(position))
        holder.itemView.setOnClickListener { clickListener.onMovieClick(films.get(position)) }
    }

    override fun getItemCount(): Int = films.size
   fun interface FilmClickListener {
    fun onMovieClick(movie: Film)
    }
}