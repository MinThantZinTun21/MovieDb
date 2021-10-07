package com.codigotext.moviedb.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import coil.network.HttpException
import com.codigotext.moviedb.data.repository.local.MovieDatabase
import com.codigotext.moviedb.data.repository.local.MovieEntity
import com.codigotext.moviedb.data.repository.remote.ApiService
import com.codigotext.moviedb.data.repository.remote.PopulatorMovieRemoteMediator
import com.codigotext.moviedb.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okio.IOException
import java.util.concurrent.TimeUnit

class MovieRepository @javax.inject.Inject constructor(
    private val remote: ApiService,
    private val local: MovieDatabase
) {
    private val movieDao = local.movieDao()

    //call without page
    fun getUpcomingMovie(
        apiKey: String,
        refresh: Boolean,
        onLoadSuccess: () -> Unit,
        onLoadFail: (Throwable) -> Unit
    ): Flow<Resource<List<MovieEntity>>> = networkBoundResource(
        query = {
            movieDao.getAllUpcomingMovie(MOVIE_TYPE.upcoming.name)
        },
        load = {
            val response = remote.getUpComingMovie(apiKey = apiKey)
            response.results
        },
        saveLoadResult = { remoteMoive ->
            //prevent fav movie  cache data override by remote data .
            val cacheMovies = movieDao.getAllUpcomingMovie(MOVIE_TYPE.upcoming.name).first()
            val movieForSave = remoteMoive.map { remote ->
                val isFav = cacheMovies.any { cache ->
                    if (remote.id == cache.id) {
                        cache.isFavourite == 1
                        //fav alway false for new remote moive data
                    } else false
                }
                MovieEntity(
                    id = remote.id,
                    title = remote.title,
                    isFavourite = isFav.ToBooleanInt(),
                    overview = remote.overview,
                    updatedAt = System.currentTimeMillis(),
                    release_date = remote.release_date,
                    poster_path = remote.poster_path,
                    type = MOVIE_TYPE.upcoming.name
                )
            }
            local.withTransaction {
                movieDao.insertMovies(movieForSave)
            }
        },
        shouldLoad = { data ->
            if (refresh) {
                true
            } else {
                val movieListSortByUpdate = data.sortedBy {
                    it.updatedAt
                }
                val lastUpdateTime = movieListSortByUpdate.firstOrNull()?.updatedAt
                val expireCache =
                    lastUpdateTime === null || lastUpdateTime < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(
                        Constant.CACHE_LIFETIME
                    )
                expireCache
            }

        },
        onLoadSuccess = {
            onLoadSuccess()
        },
        onLoadError = { t ->
            if (t !is HttpException && t !is IOException) {
                throw  t
            }
            onLoadFail(t)
        }
    )
    @ExperimentalPagingApi
    fun getPopularMovie(
    ): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        remoteMediator = PopulatorMovieRemoteMediator(remote, local),
        pagingSourceFactory = { local.movieDao().getMoviePageSource(MOVIE_TYPE.populator.name) }
    ).flow


    suspend fun updateMovie(movie: MovieEntity) {
        local.movieDao().updateMovie(movie)

    }

}