package com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prasoon.themoviedatabasejetpackcompose.ui.PopularMoviesScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.SearchScreen

@Composable
fun BottomAppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            PopularMoviesScreen(navController)
        }
        composable(route = Screen.Search.route) {
            SearchScreen()
        }
    }

}