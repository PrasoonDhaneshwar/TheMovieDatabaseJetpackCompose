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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prasoon.themoviedatabasejetpackcompose.R
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.NavScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.movies.MovieItem
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.SearchUiState
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.SearchViewModel

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val searchText by viewModel.searchMoviesText.collectAsState()
    val searchMoviesList by viewModel.searchMovieList.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val TAG = "SearchScreen"

    // Derive the UI state
    val uiState = when {
        errorMessage != null -> SearchUiState.Error(errorMessage!!)
        isLoading || isSearching -> SearchUiState.Loading // Show loading all while network call is pending
        searchText.length > 2 && searchMoviesList.isEmpty() -> SearchUiState.Empty // Only empty if done searching and no results
        searchMoviesList.isNotEmpty() -> SearchUiState.Success(searchMoviesList)
        else -> SearchUiState.Idle
    }

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
            placeholder = { Text(text = stringResource(R.string.search_movie_by_title)) }
        )
        Log.d(
            TAG, "isSearching: $isSearching " +
                    "searchMoviesList is empty: ${searchMoviesList.isEmpty()}, " +
                    "errorMessage: $errorMessage, isLoading: $isLoading"
        )

        if (searchText.length > 2) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState) {
                    is SearchUiState.Loading -> Text(stringResource(R.string.loading), modifier = Modifier.align(Alignment.Center))
                    is SearchUiState.Searching -> Text(stringResource(R.string.searching), modifier = Modifier.align(Alignment.Center))
                    is SearchUiState.Error -> Text(uiState.message, modifier = Modifier.align(Alignment.Center))
                    is SearchUiState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = uiState.movies) { movie ->
                            MovieItem(movie = movie, onItemClick = {
                                navController.navigate(
                                    NavScreen.MovieDetailScreen.route +
                                            "/${movie.id}?source=search"
                                )
                            })
                        }
                    }
                    is SearchUiState.Empty -> Text(stringResource(R.string.no_movies_available), modifier = Modifier.align(Alignment.Center))
                    SearchUiState.Idle -> Text(stringResource(R.string.search_for_a_movie), modifier = Modifier.align(Alignment.Center))
                }
            }
        } else if (errorMessage != null && errorMessage!!.isNotEmpty()) {
            Text(
                text = errorMessage ?: "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxHeight()
            )
        }
    }
}
