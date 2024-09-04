package com.prasoon.themoviedatabasejetpackcompose.domain.repository

import com.prasoon.themoviedatabasejetpackcompose.common.Resource
import com.prasoon.themoviedatabasejetpackcompose.domain.model.PopularMovies

interface MoviesRepository {
    suspend fun getSearchMovieTitle(query: String): Resource<PopularMovies>

    suspend fun getPopularMovies(page: Int): PopularMovies
}