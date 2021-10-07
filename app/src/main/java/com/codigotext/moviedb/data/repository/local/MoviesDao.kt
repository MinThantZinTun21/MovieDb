package com.codigotext.moviedb.data.repository.local

import androidx.paging.PagingSource
import androidx.room.*
import com.codigotext.moviedb.data.model.RemoteKey
import com.codigotext.moviedb.util.MOVIE_TYPE
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(articles: List<MovieEntity>)

    @Query("SELECT * FROM movie where type = :movieType ")
    fun getAllUpcomingMovie(movieType: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie  where isFavourite=1 and type= :movieType")
    fun getFavouite(movieType: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie where type= :type")
    fun getMoviePageSource(type: String): PagingSource<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(key: List<RemoteKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(key: RemoteKey)

    @Query("SELECT * FROM remotekey WHERE id = :id")
    suspend fun getRemoteKey(id: Int): RemoteKey

    @Query("SELECT * FROM remotekey")
    suspend fun getAllRemoteKey(): List<RemoteKey>

    @Query("DELETE FROM remotekey")
    fun clearAllRemoteKey()

    @Update
    suspend fun updateMovie(movie: MovieEntity)


    @Query("DELETE FROM movie")
    fun clearAllMovie()

}