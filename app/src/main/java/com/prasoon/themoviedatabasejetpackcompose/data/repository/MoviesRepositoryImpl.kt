package com.prasoon.themoviedatabasejetpackcompose.data.repository

import android.util.Log
import com.prasoon.themoviedatabasejetpackcompose.common.Constants.BASE_URL
import com.prasoon.themoviedatabasejetpackcompose.common.Resource
import com.prasoon.themoviedatabasejetpackcompose.data.remote.dto.MovieDto
import com.prasoon.themoviedatabasejetpackcompose.data.remote.dto.PopularMoviesDto
import com.prasoon.themoviedatabasejetpackcompose.data.toPopularMovies
import com.prasoon.themoviedatabasejetpackcompose.domain.model.PopularMovies
import com.prasoon.themoviedatabasejetpackcompose.domain.repository.MoviesRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject

private const val POPULAR_MOVIES = "$BASE_URL/3/movie/popular"
private const val SEARCH_MOVIES = "$BASE_URL/3/search/movie"
private const val TAG = "MoviesRepositoryImpl"

class MoviesRepositoryImpl @Inject constructor(private val httpClient: HttpClient) :
    MoviesRepository {

    override suspend fun getPopularMovies(page: Int): Resource<PopularMovies> {
        return try {

            val response = httpClient.get<PopularMoviesDto> {
                url(POPULAR_MOVIES)
                parameter("page", page)  // Add page parameter to the query
            }.toPopularMovies()
            Log.i(TAG, "$response")

            Resource.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is IOException -> Resource.Failure(Exception("No internet connection"))
                is TimeoutCancellationException -> Resource.Failure(Exception("Request timed out"))
                else -> Resource.Failure(e)
            }
        }
    }

    override suspend fun getSearchMovieTitle(query: String): Resource<PopularMovies> {
        return try {
            val response = httpClient.get<PopularMoviesDto> {
                url(SEARCH_MOVIES)
                parameter("query", query)
            }.toPopularMovies()

            Resource.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is IOException -> Resource.Failure(Exception("No internet connection"))
                is TimeoutCancellationException -> Resource.Failure(Exception("Request timed out"))
                else -> Resource.Failure(e)
            }
        }
    }
}