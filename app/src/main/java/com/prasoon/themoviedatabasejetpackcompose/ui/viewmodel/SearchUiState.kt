package com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel

import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    object Searching : SearchUiState()
    data class Error(val message: String) : SearchUiState()
    data class Success(val movies: List<Movie>) : SearchUiState()
    object Empty : SearchUiState()
}
