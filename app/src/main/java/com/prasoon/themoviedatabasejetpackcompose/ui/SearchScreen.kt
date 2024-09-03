package com.prasoon.themoviedatabasejetpackcompose.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prasoon.themoviedatabasejetpackcompose.ui.movies.MovieItem
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.MainViewModel
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.MovieViewIntent

@Composable
fun SearchScreen(viewModel: MainViewModel = hiltViewModel()) {
    val movies by viewModel.popularMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val searchText by viewModel.searchMoviesText.collectAsState()
    val searchMoviesList by viewModel.searchMovieList.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Column {
        Text(
            text = "SEarch",
            fontWeight = FontWeight(900),
            fontFamily = FontFamily.Serif,
            fontSize = 32.sp,
            modifier = Modifier.padding(8.dp)
        )

        TextField(value = searchText, onValueChange = viewModel::onSearchMovieChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search Movie by title...") }
        )
        Log.d("HomeScreen", "isSearching: $isSearching")

        if (isSearching) {
            Log.d("HomeScreen", "currentSearchText: $searchMoviesList")
            Box(modifier = Modifier.fillMaxSize()) {
                if (searchMoviesList.isEmpty() && !isSearching /*&& errorMessage == null*/) {
                    // Show message if no movies found
                    Text(
                        text = "No movies available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = searchMoviesList) { movie ->
                            MovieItem(movie = movie, onItemClick = {
                                //navController.navigate(NavScreen.MovieDetailScreen.route + "/${movie.id}")

                            })
                        }

                        // Detect when the user scrolls to the end of the list
//                    item {
//                        if (viewModel.currentPage <= viewModel.totalPages && !isLoading) {
//                            LaunchedEffect(Unit) {
//                                viewModel.loadPopularMovies()  // Load more data when scrolled to end
//                            }
//                            CircularProgressIndicator(Modifier.padding(16.dp))  // Show a loading spinner
//                        }
//                    }
                    }
                }
                // Show loading spinner at the center when loading todo
                if (isSearching && searchMoviesList.isEmpty() /*&& !errorMessage.isNullOrEmpty()*/) {
                    Text(
                        text = errorMessage.toString(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }

        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                if (movies.isEmpty() && !isLoading && errorMessage == null) {
                    // Show message if no movies found
                    Text(
                        text = "No movies available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = movies) { movie ->
                            MovieItem(movie = movie, onItemClick = {
                                //navController.navigate(NavScreen.MovieDetailScreen.route + "/${movie.id}")

                            })
                        }

                        // Detect when the user scrolls to the end of the list
                        item {
                            if (viewModel.currentPage <= viewModel.totalPages && !isLoading) {
                                LaunchedEffect(Unit) {
                                    viewModel.loadPopularMovies()  // Load more data when scrolled to end
                                }
                                CircularProgressIndicator(Modifier.padding(16.dp))  // Show a loading spinner
                            }
                        }
                    }
                }
                // Show loading spinner at the center when loading todo
                if (isLoading && movies.isEmpty() && !errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage.toString(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                LaunchedEffect(Unit) {
                    viewModel.processIntent(MovieViewIntent.LoadPopularMovies)
                }
            }
        }
    }
}
