package com.codigotext.moviedb.data.repository.remote

import com.codigotext.moviedb.data.model.response.ResponseMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //https://api.themoviedb.org/3/movie/upcoming
    @GET("movie/upcoming")
    suspend fun getUpComingMovie(
        @Query("api_key") apiKey: String,
    ): ResponseMovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") apiKey: String,
        @Query("page") page: String,

    ): ResponseMovieResponse

}