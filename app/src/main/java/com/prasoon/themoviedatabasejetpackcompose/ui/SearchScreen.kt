package com.prasoon.themoviedatabasejetpackcompose.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.NavScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.movies.MovieItem
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.SearchViewModel

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val searchText by viewModel.searchMoviesText.collectAsState()
    val searchMoviesList by viewModel.searchMovieList.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val TAG = "SearchScreen"

    Column {
        Text(
            text = "TMDB",
            fontWeight = FontWeight(900),
            fontFamily = FontFamily.Serif,
            fontSize = 32.sp,
            modifier = Modifier.padding(8.dp)
        )

        TextField(value = searchText, onValueChange = viewModel::onSearchMovieChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search Movie by title...") }
        )
        Log.d(
            TAG, "isSearching: $isSearching " +
                    "searchMoviesList is empty: ${searchMoviesList.isEmpty()}, " +
                    "errorMessage: $errorMessage, isLoading: $isLoading"
        )

        if (searchText.length > 2) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading && errorMessage.isNullOrEmpty()) {
                    Text(text = "Loading...", modifier = Modifier.align(Alignment.Center))
                } else if (isSearching && errorMessage.isNullOrEmpty()) {
                    Text(text = "Searching...", modifier = Modifier.align(Alignment.Center))
                } else if (!isSearching && !isLoading &&
                    errorMessage.isNullOrEmpty() &&
                    searchMoviesList.isEmpty()
                ) {
                    Text(text = "No movies available.", modifier = Modifier.align(Alignment.Center))
                } else if (!errorMessage.isNullOrEmpty()) {
                    // In case of exception
                    Text(
                        text = errorMessage.toString(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = searchMoviesList) { movie ->
                            MovieItem(movie = movie, onItemClick = {
                                Log.i(
                                    TAG, "clicked item, " +
                                            "movie.id ${movie.id}. Navigate to " +
                                            "${NavScreen.MovieDetailScreen.route}/${movie.id}"
                                )
                                navController.navigate(
                                    NavScreen.MovieDetailScreen.route +
                                            "/${movie.id}?source=search"
                                )
                            })
                        }
                    }
                }
            }
        } else if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage.toString(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxHeight()
            )
        }
    }
}
