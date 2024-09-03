package com.prasoon.themoviedatabasejetpackcompose.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularMoviesDto(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val movieDtos: List<MovieDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)