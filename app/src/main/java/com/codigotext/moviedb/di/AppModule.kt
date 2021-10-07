package com.codigotext.moviedb.di

import android.app.Application
import androidx.room.Room
import com.codigotext.moviedb.data.repository.local.MovieDatabase
import com.codigotext.moviedb.data.repository.remote.ApiService
import com.codigotext.moviedb.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton

    fun provideOkhttp(inteceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(inteceptor)
        .build()


    @Provides
    @Singleton
    fun provideInteceptor() = HttpLoggingInterceptor().also {
        it.apply { it.level = HttpLoggingInterceptor.Level.BODY };
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideDb(application: Application): MovieDatabase =
        Room.databaseBuilder(application, MovieDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()

}