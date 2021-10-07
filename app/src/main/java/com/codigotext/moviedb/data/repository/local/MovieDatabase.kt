package com.codigotext.moviedb.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codigotext.moviedb.data.model.RemoteKey
import com.codigotext.moviedb.util.Constant


@Database(
    entities = [MovieEntity::class,RemoteKey::class],
    version = Constant.DB_VERSION
)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}