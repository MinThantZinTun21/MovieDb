package com.codigotext.moviedb.data.model.response

import com.codigotext.moviedb.data.repository.local.MovieEntity

data class ResponseMovieResponse(
    val results: ArrayList<MovieEntity>
)