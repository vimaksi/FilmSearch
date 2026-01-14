package com.example.filmsearch

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.w3c.dom.Text

class FilmsViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
    .inflate(R.layout.film_item, parent, false)) {

        val album: ImageView = itemView.findViewById(R.id.filmAlbum)
        val title: TextView = itemView.findViewById(R.id.filmName)
        val description: TextView = itemView.findViewById(R.id.filmDescription)
        fun bind(model: Film) {
            title.text = model.title
            description.text = model.description

            Glide.with(itemView)
            .load(model.image)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .into(album)
        }
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}