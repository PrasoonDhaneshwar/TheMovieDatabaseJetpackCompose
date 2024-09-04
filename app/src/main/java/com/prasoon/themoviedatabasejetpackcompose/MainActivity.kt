package com.prasoon.themoviedatabasejetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.prasoon.themoviedatabasejetpackcompose.ui.MainScreen
import com.prasoon.themoviedatabasejetpackcompose.ui.theme.TheMovieDatabaseJetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheMovieDatabaseJetpackComposeTheme {
                MainScreen()
            }
        }
    }
}