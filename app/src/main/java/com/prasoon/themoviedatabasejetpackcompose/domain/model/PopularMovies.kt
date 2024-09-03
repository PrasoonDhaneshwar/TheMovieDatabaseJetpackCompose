package com.prasoon.themoviedatabasejetpackcompose.domain.model

data class PopularMovies(
    val page: Int,
    val movies: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)
