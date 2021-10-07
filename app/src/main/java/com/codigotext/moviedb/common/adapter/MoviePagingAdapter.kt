package com.codigotext.moviedb.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.codigotext.moviedb.R
import com.codigotext.moviedb.common.comparator.MovieComaparator
import com.codigotext.moviedb.data.repository.local.MovieEntity
import com.codigotext.moviedb.util.Constant

class MoviePagingAdapter(
    private val onItemClick: (MovieEntity) -> Unit
) : PagingDataAdapter<MovieEntity, MoviePagingAdapter.ViewHolderMovie>(MovieComaparator()) {
    class ViewHolderMovie(v: View) : RecyclerView.ViewHolder(
        v
    ) {

    }

    override fun onBindViewHolder(holder: ViewHolderMovie, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick(getItem(position)!!)
        }
        holder.itemView.findViewById<TextView>(R.id.tvTitle).text = getItem(position)!!.title
        holder.itemView.findViewById<ImageView>(R.id.ivPoster)
            .load("${Constant.IMAGE_URL}${getItem(position)!!.poster_path}")

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovie {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolderMovie(view)
    }

}