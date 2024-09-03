package com.prasoon.themoviedatabasejetpackcompose.ui.movies

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie

@Composable
fun SearchMovieList(movies: List<Movie>) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(movies) { movie ->
                MovieItem(movie) {
                    //Text(text = movie.title, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}