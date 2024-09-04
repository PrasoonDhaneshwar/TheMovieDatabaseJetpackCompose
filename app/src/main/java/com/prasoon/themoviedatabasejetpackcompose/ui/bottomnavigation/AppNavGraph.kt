package com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prasoon.themoviedatabasejetpackcompose.ui.PopularMoviesScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.SearchScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.movies.MovieDetailScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.PopularMoviesViewModel
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.SearchViewModel

@Composable
fun AppNavGraph(navController: NavHostController, popularMoviesViewModel: PopularMoviesViewModel, searchViewModel: SearchViewModel) {
    val TAG = "BottomAppNavGraph"
    NavHost(navController = navController, startDestination = NavScreen.HomeScreen.route) {
        composable(route = NavScreen.SearchScreen.route) {
            SearchScreen(navController, searchViewModel)
        }
        composable(
            route = NavScreen.HomeScreen.route
        ) {
            PopularMoviesScreen(navController, popularMoviesViewModel)
        }
        composable(
            route = NavScreen.MovieDetailScreen.route + "/{id}?source={source}"
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("id") ?: ""
            val source = backStackEntry.arguments?.getString("source") ?: ""
            Log.i(TAG, "Navigation backStackEntry.arguments: ${backStackEntry.arguments}, movieId: $movieId")
            MovieDetailScreen(movieId = Integer.parseInt(movieId), source, popularMoviesViewModel, searchViewModel)
        }
    }
}