package com.example.filmsearch

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilmsAdapter(val clickListener: FilmClickListener): RecyclerView.Adapter<FilmsViewHolder>() {
    var films = ArrayList<Film>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder = FilmsViewHolder(parent)

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