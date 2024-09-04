package com.prasoon.themoviedatabasejetpackcompose.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie
import com.prasoon.themoviedatabasejetpackcompose.domain.use_case.GetPopularMoviesUseCase

class MoviesPagingSource(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    ) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = getPopularMoviesUseCase(page)
            LoadResult.Page(
                data = response.movies,
                prevKey = null,
                nextKey = if (response.movies.isNotEmpty()) response.page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}