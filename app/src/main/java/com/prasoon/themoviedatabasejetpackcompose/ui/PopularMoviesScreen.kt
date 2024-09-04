package com.prasoon.themoviedatabasejetpackcompose.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.prasoon.themoviedatabasejetpackcompose.common.ErrorItem
import com.prasoon.themoviedatabasejetpackcompose.common.LoadingItem
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.NavScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.movies.MovieItem
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.PopularMoviesViewModel

@Composable
fun PopularMoviesScreen(navController: NavController, viewModel: PopularMoviesViewModel) {
    val TAG = "PopularMoviesScreen"
    val pagedMovieList = viewModel.moviesPager.collectAsLazyPagingItems()

    Column {
        Text(
            text = "TMDB",
            fontWeight = FontWeight(900),
            fontFamily = FontFamily.Serif,
            fontSize = 32.sp,
            modifier = Modifier.padding(2.dp)
        )
        Text(
            text = "Now Playing",
            fontWeight = FontWeight(900),
            fontFamily = FontFamily.Cursive,
            fontSize = 25.sp,
            color = Color.Gray,
            modifier = Modifier.padding(2.dp)
        )

        Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(pagedMovieList.itemCount) { index ->
                        pagedMovieList[index]?.let { movie ->
                            viewModel.updateMovieList(movie)
                            MovieItem(movie = movie, onItemClick = {
                                Log.i(TAG, "Got the MOVIE ID: ${it.id}")
                                Log.i(TAG,"clicked item: ${NavScreen.MovieDetailScreen.route + "/${it.id}"}"
                                )
                                navController.navigate(NavScreen.MovieDetailScreen.route + "/${it.id}?source=home")
                            })
                        }
                    }
                    when(pagedMovieList.loadState.append) {
                        is LoadState.Error -> {
                            item {
                                (pagedMovieList.loadState.append as LoadState.Error).error.message?.let {
                                    ErrorItem(it)
                                }
                            }
                        }
                        LoadState.Loading -> {
                            item {
                                LoadingItem()
                            }
                        }
                        is LoadState.NotLoading -> Unit
                    }

                    when(pagedMovieList.loadState.refresh) {
                        is LoadState.Error -> Unit
                        LoadState.Loading -> {
                            item {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.fillParentMaxHeight())
                                }
                            }
                        }
                        is LoadState.NotLoading -> Unit
                    }
                }
        }
    }
}
