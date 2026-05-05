package com.example.filmsearch.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movies_table")
data class MovieEntity(
   @PrimaryKey
    val id: String,
    val resultType: String,
    val image: String,
    val title: String,
    val description: String)