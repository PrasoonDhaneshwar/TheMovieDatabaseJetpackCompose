package com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prasoon.themoviedatabasejetpackcompose.common.Resource
import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie
import com.prasoon.themoviedatabasejetpackcompose.domain.use_case.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {

    private val TAG = "PopularMoviesViewModel"

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    var currentPage = 1
    var totalPages = 1

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        Log.i(TAG, "loadPopularMovies()")

        if (_isLoading.value || currentPage > totalPages) return

        viewModelScope.launch {
            _isLoading.value = true

            val resource = getPopularMoviesUseCase.invoke(currentPage)
            Log.i(TAG, "loadPopularMovies() resource: $resource")

            when (resource) {
                is Resource.Success -> {
                    val popularMovies = resource.result
                    if (popularMovies.movies.isEmpty()) {
                        _errorMessage.value = "No movies found"
                    } else {
                        _popularMovies.value += popularMovies.movies  // Append new data
                        totalPages = popularMovies.totalPages  // Update total pages
                        currentPage++  // Increment to the next page
                    }
                }

                is Resource.Failure -> {
                    _errorMessage.value = resource.exception.message  // Set the error message
                }

                is Resource.Loading -> {
                    _isLoading.value = false
                }
            }
            _isLoading.value = false
        }
    }

    fun getMovieById(movieId: Int?): Movie? {
        Log.i(TAG, "Got the movieId: $movieId")
        Log.i(TAG, "Got _popularMovies: ${_popularMovies.value}")

        val movie = _popularMovies.value.find { it.id == movieId }
        Log.i(TAG, "Got the MOVIE: $movie")

        return movie

    }

}