package com.codigotext.moviedb.common.comparator

import androidx.recyclerview.widget.DiffUtil
import com.codigotext.moviedb.data.repository.local.MovieEntity

class MovieComaparator : DiffUtil.ItemCallback<MovieEntity>() {
    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id

    }
    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id

    }
}