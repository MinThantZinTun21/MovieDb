package com.codigotext.moviedb.ui.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codigotext.moviedb.data.repository.MovieRepository
import com.codigotext.moviedb.data.repository.local.MovieEntity
import com.codigotext.moviedb.util.ToBooleanInt
import com.codigotext.moviedb.util.ToInBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    fun toggleFav(data: MovieEntity) {
        viewModelScope.launch {
            repository.updateMovie(data)
        }
    }

}