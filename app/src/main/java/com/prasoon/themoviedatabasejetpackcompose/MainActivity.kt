package com.prasoon.themoviedatabasejetpackcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prasoon.themoviedatabasejetpackcompose.ui.PopularMoviesScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.movies.MovieDetailScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.MainViewModel
import com.prasoon.themoviedatabasejetpackcompose.ui.theme.TheMovieDatabaseJetpackComposeTheme
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.NavScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge() todo
        setContent {
            TheMovieDatabaseJetpackComposeTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavScreen.HomeScreen.route
                    ) {
                        composable(
                            route = NavScreen.HomeScreen.route
                        ) {
                            PopularMoviesScreen(navController)
                        }
                        composable(
                            route = NavScreen.MovieDetailScreen.route + "/{id}"
                        ) {
                            val movieId = it.arguments?.getString("id")
                            Log.i("MainActivity", "Got the MOVIE ID: $movieId")
                            Log.i("MainActivity", "Got the MOVIE ID: ${it.arguments}")
                            viewModel.loadPopularMovies()
                            val movie = viewModel.getMovieById(Integer.parseInt(movieId))
                            movie?.let { MovieDetailScreen(movie = movie) }
                        }
                    }
                }

//                HomeScreen(viewModel)
                //MainScreen()
            }
        }
    }
}