package com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel

import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie

data class PopularMoviesState(
    val isLoading: Boolean = false,
    var movies: List<Movie> = emptyList(),
    val errorMessage: String = "",
)