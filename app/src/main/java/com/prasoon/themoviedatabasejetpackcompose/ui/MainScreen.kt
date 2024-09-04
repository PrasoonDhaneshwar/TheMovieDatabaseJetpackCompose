package com.prasoon.themoviedatabasejetpackcompose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.BottomAppNavGraph
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.PopularMoviesViewModel
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.SearchViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val popularMoviesViewModel: PopularMoviesViewModel = hiltViewModel() // Obtain the ViewModel instance
    val searchViewModel: SearchViewModel = hiltViewModel() // Obtain the ViewModel instance

    Scaffold(
        bottomBar = { BottomBar(navController = navController)
        }
    ) {
        Column(Modifier.padding(it)) {
            BottomAppNavGraph(navController = navController, popularMoviesViewModel, searchViewModel)
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        BottomNavItem("Home", "movie_list_screen", Icons.Filled.Home),
        BottomNavItem("Search", "movie_search_screen", Icons.Filled.Search)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)