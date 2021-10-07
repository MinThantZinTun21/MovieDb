package com.codigotext.moviedb.ui.upcoming_movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codigotext.moviedb.data.model.Event
import com.codigotext.moviedb.data.repository.MovieRepository
import com.codigotext.moviedb.util.Constant
import com.codigotext.moviedb.util.RefreshModel
import com.codigotext.moviedb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingMovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    var scrollingToTopAfterRefresh = false

    private val eventChannel = Channel<Event> { }
    val events = eventChannel.receiveAsFlow()

    private val reloadChannel = Channel<RefreshModel>()
    private val reloadTrigger = reloadChannel.receiveAsFlow()

    //get upcoming movie without using paganation
    val upcomingMovie = reloadTrigger.flatMapLatest {
        repository.getUpcomingMovie(
            Constant.API_KEY,
            refresh = it == RefreshModel.FORCE,
            onLoadSuccess = {
                scrollingToTopAfterRefresh = true
            },
            onLoadFail = {
                viewModelScope.launch {
                    eventChannel.send(Event.showErrorMessage(it))
                }

            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    fun onStart() {
        if (upcomingMovie.value !is Resource.Loading) {
            viewModelScope.launch {
                reloadChannel.send(RefreshModel.FORCE)
            }
        }

    }

    fun onRefresh() {
        if (upcomingMovie.value !is Resource.Loading) {
            viewModelScope.launch {
                reloadChannel.send(RefreshModel.FORCE)
            }
        }
    }


}