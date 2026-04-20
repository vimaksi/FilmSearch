package com.example.filmsearch.ui.name

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmsearch.R
import com.example.filmsearch.domain.models.Name

class NameViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.name_item, parent, false)){

    var photo: ImageView = itemView.findViewById(R.id.nameImage)
    var name: TextView = itemView.findViewById(R.id.name)
    var description: TextView = itemView.findViewById(R.id.title)

    fun bind(nameN: Name) {
        Glide.with(itemView)
            .load(nameN.image)
            .placeholder(R.drawable.ic_placeholder_45)
            .circleCrop()
            .into(photo)

        name.text = nameN.name
        description.text = nameN.description
    }
}