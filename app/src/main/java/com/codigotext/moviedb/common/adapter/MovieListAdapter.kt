package com.codigotext.moviedb.common.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.codigotext.moviedb.R
import com.codigotext.moviedb.common.comparator.MovieComaparator
import com.codigotext.moviedb.data.repository.local.MovieEntity
import com.codigotext.moviedb.util.Constant


class MovieListAdapter(
    private val onMovieItemClick: (MovieEntity) -> Unit
) : ListAdapter<MovieEntity, MovieListAdapter.ViewHolderMovie>(MovieComaparator()) {

    class ViewHolderMovie(v: View) : RecyclerView.ViewHolder(
        v
    ) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovie {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolderMovie(view)
    }

    override fun onBindViewHolder(holder: ViewHolderMovie, position: Int) {
        holder.itemView.setOnClickListener {
            onMovieItemClick(getItem(position!!))
        }
        holder.itemView.findViewById<TextView>(R.id.tvTitle).text = getItem(position).title
        holder.itemView.findViewById<ImageView>(R.id.ivPoster)
            .load("${Constant.IMAGE_URL}${getItem(position).poster_path}")
    }

}