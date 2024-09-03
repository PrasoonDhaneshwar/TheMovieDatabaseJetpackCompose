package com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel

sealed class MovieViewIntent {
    object LoadPopularMovies: MovieViewIntent()
    data class SearchMovies(val query: String): MovieViewIntent()
}