package com.example.filmsearch.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmsearch.R
import com.example.filmsearch.domain.models.Film

class HistoryViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
    .inflate(R.layout.list_item_history, parent, false)) {

        var cover: ImageView = itemView.findViewById(R.id.cover)
        var title: TextView = itemView.findViewById(R.id.title)
        var description: TextView = itemView.findViewById(R.id.description)

        fun bind(movie: Film) {
            Glide.with(itemView)
                .load(movie.image)
                .into(cover)

            title.text = movie.title
            description.text = movie.description
        }
}