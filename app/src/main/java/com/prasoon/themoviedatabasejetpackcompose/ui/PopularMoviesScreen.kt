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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prasoon.themoviedatabasejetpackcompose.ui.movies.MovieItem
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.MainViewModel
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.MovieViewIntent
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.NavScreen

@Composable
fun PopularMoviesScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val movies by viewModel.popularMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val searchText by viewModel.searchMoviesText.collectAsState()
    val searchMoviesList by viewModel.searchMovieList.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

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
        Log.d("HomeScreen", "isSearching: $isSearching searchMoviesList.isNotEmpty(): ${searchMoviesList.isNotEmpty()}" +
                " movies.isNotEmpty() ${movies.isNotEmpty()}")

        if (searchText.length > 2 && searchMoviesList.isNotEmpty()) {
            //
            Log.i("HomeScreen", "searchMoviesList: $searchMoviesList")
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = searchMoviesList) { movie ->
                        Text(
                            text = movie.title,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
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
                } else if (isLoading) {
                    Text(
                        text = "Loading...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = movies) { movie ->
                            MovieItem(movie = movie, onItemClick = {
                                Log.i("HomeScreen", "Got the MOVIE ID: ${movie.id}")
                                Log.i("HomeScreen", "Got the MOVIE ID: MovieDetailScreen ${NavScreen.MovieDetailScreen.route + "/${movie.id}"}")

                                navController.navigate(NavScreen.MovieDetailScreen.route + "/${movie.id}")

                            })
                            Log.i("HomeScreen", "Loading...")

                        }

                        // Detect when the user scrolls to the end of the list
                        item {
                            if (viewModel.currentPage <= viewModel.totalPages && errorMessage.isNullOrEmpty()) {
                                Log.i("HomeScreen", "viewModel.currentPage loadPopularMovies() viewModel.currentPage: ${viewModel.currentPage}" +
                                        "viewModel.totalPages: ${viewModel.totalPages}")

                                LaunchedEffect(Unit) {
                                    Log.i("HomeScreen", "load new loadPopularMovies()")
                                    viewModel.loadPopularMovies()  // Load more data when scrolled to end
                                }
                                CircularProgressIndicator(Modifier.padding(16.dp), color = Color.Blue)  // Show a loading spinner
                            } else {
                                Text(
                                    text = errorMessage.toString(),
                                )
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
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp), color = Color.Red)
                }
            }
        }


    }
}
