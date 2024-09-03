package com.prasoon.themoviedatabasejetpackcompose.domain.model

data class Movie(
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val title: String,
    val overview: String,
    val voteAverage: Double,
    val releaseDate: String,
    val fullPosterPath: String
) {
    fun doesMatchSearchQuery(currentSearchText: String): Boolean {
        val matchingCombinations = listOf(
            "$originalTitle$title",
            "$title$releaseDate",
            title
            )
        return matchingCombinations.any {
            it.contains(currentSearchText, ignoreCase = true)
        }
    }
}
