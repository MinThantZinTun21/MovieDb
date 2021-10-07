package com.codigotext.moviedb.data.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//0 false / 1 true
@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val isFavourite: Int=0,
    val overview: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val release_date: String,
    val poster_path: String,
    val type:String
):Serializable