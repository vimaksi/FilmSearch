package com.example.filmsearch.ui.name

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.ui.films.FilmsViewHolder


class NameAdapter(): RecyclerView.Adapter<NameViewHolder>() {
    var names = ArrayList<Name>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder = NameViewHolder(parent)
    override fun onBindViewHolder(
        holder: NameViewHolder,
        position: Int
    ) {
        holder.bind(names.get(position))
        //holder.itemView.setOnClickListener { clickListener.onMovieClick(films.get(position)) }
    }

    override fun getItemCount(): Int = names.size
}