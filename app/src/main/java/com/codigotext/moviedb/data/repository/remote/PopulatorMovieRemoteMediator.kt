package com.codigotext.moviedb.data.repository.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.codigotext.moviedb.data.model.RemoteKey
import com.codigotext.moviedb.data.repository.local.MovieDatabase
import com.codigotext.moviedb.data.repository.local.MovieEntity
import com.codigotext.moviedb.util.Constant
import com.codigotext.moviedb.util.MOVIE_TYPE
import com.codigotext.moviedb.util.ToBooleanInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val START_PAGE_INDEX = 1

@ExperimentalPagingApi
class PopulatorMovieRemoteMediator
    (
    private val apiService: ApiService,
    private val movieDb: MovieDatabase,
) : RemoteMediator<Int, MovieEntity>() {
    private val localData = movieDb.movieDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }

        }

        try {

            val response = apiService.getPopularMovie(
                Constant.API_KEY,
                page.toString(),
            )
            val remoteMovies = response.results
            val localfavMovies = localData.getFavouite(MOVIE_TYPE.populator.name).first()
            //handle fav for not to override by refresh new data
            val movies = remoteMovies.map { remoteMovies ->
                val isFav = localfavMovies.any { localfavMovies ->
                    localfavMovies.isFavourite == remoteMovies.isFavourite
                }

                MovieEntity(
                    id = remoteMovies.id,
                    title = remoteMovies.title,
                    isFavourite = isFav.ToBooleanInt(),
                    overview = remoteMovies.overview,
                    release_date = remoteMovies.release_date,
                    poster_path = remoteMovies.poster_path,
                    type = MOVIE_TYPE.populator.name
                )
            }
            movieDb.withTransaction {
                //val lastposition=da
                val prevKey = if (page == START_PAGE_INDEX) null else page - 1
                val nextKey = page + 1
                val key = remoteMovies.map {
                    RemoteKey(it.id, nextKey, prevKey)
                }
                localData.insertRemoteKey(key)
                localData.insertMovies(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)

        }
    }


    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRefreshRemoteKey(state)
                remoteKeys?.nextPageKey?.minus(1) ?: START_PAGE_INDEX

            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.previousKey ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)

                val nextKey = remoteKeys?.nextPageKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey

            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKey? {
        return withContext(Dispatchers.IO) {
            state.pages
                .firstOrNull { it.data.isNotEmpty() }
                ?.data?.firstOrNull()
                ?.let { movie -> localData.getRemoteKey(movie.id) }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH

    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKey? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { data ->
                // Get the remote keys of the last item retrieved
                localData.getRemoteKey(data.id)
            }
    }
    private suspend fun getRefreshRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKey? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { id ->
                    localData.getRemoteKey(id)
                }
            }
        }
    }
}

