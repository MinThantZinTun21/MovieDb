package com.codigotext.moviedb.ui.populator_movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.codigotext.moviedb.data.repository.MovieRepository
import com.codigotext.moviedb.util.RefreshModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopulatorMovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val loadMovieChannel = Channel<String>() {}
    private val loadMovieTrigger = loadMovieChannel.receiveAsFlow()
    var onprogress = false
    @ExperimentalPagingApi
    val movie = loadMovieTrigger.flatMapLatest {
        repository.getPopularMovie().cachedIn(viewModelScope)
    }.cachedIn(viewModelScope)

     fun onStart() {
      viewModelScope.launch {
          loadMovieChannel.send("_")
          onprogress=true
      }

    }


}