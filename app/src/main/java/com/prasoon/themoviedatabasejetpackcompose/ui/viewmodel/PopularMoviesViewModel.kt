package com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.prasoon.themoviedatabasejetpackcompose.data.MoviesPagingSource
import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie
import com.prasoon.themoviedatabasejetpackcompose.domain.use_case.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {

    private val TAG = "PopularMoviesViewModel"

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())

    val moviesPager = Pager(
        PagingConfig(pageSize = 10)
    ) {
        MoviesPagingSource(getPopularMoviesUseCase)
    }.flow.cachedIn(viewModelScope)

    fun getMovieById(movieId: Int?): Movie? {
        Log.i(TAG, "getMovieById() movieId: $movieId")
        Log.i(TAG, "getMovieById() _popularMovies: ${_popularMovies.value}")

        val movie = _popularMovies.value.find { it.id == movieId }
        Log.i(TAG, "getMovieById() movie: $movie")

        return movie
    }

    fun updateMovieList(movie: Movie) {
        _popularMovies.value += movie
    }
}