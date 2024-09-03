package com.prasoon.themoviedatabasejetpackcompose.data

import com.prasoon.themoviedatabasejetpackcompose.data.remote.dto.MovieDto
import com.prasoon.themoviedatabasejetpackcompose.data.remote.dto.PopularMoviesDto
import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie
import com.prasoon.themoviedatabasejetpackcompose.domain.model.PopularMovies

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        overview = overview,
        title = title,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        fullPosterPath = "https://image.tmdb.org/t/p/original/$posterPath"
    )
}

fun PopularMoviesDto.toPopularMovies(): PopularMovies {
    return PopularMovies(
        page = page,
        movies = movieDtos.map { it.toMovie() },
        totalPages = totalPages,
        totalResults = totalResults
    )
}