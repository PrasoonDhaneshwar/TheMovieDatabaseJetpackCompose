package com.prasoon.themoviedatabasejetpackcompose.domain.use_case

import com.prasoon.themoviedatabasejetpackcompose.common.Resource
import com.prasoon.themoviedatabasejetpackcompose.domain.model.PopularMovies
import com.prasoon.themoviedatabasejetpackcompose.domain.repository.MoviesRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(page: Int): Resource<PopularMovies> {
        return repository.getPopularMovies(page)
    }
}