package com.prasoon.themoviedatabasejetpackcompose.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.BottomAppNavGraph
import com.prasoon.themoviedatabasejetpackcompose.ui.bottomnavigation.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomBar(navController = navController) }) {
        BottomAppNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        Screen.Home,
        Screen.Search
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    NavigationBarItem(selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        }, icon = {
            Icon(imageVector = screen.icon, contentDescription = "Navigation Icon")
        })
}