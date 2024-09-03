package com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation

sealed class NavScreen(val route: String) {
    object HomeScreen: NavScreen("movie_list_screen")
    object MovieDetailScreen: NavScreen("movie_detail_screen")
}