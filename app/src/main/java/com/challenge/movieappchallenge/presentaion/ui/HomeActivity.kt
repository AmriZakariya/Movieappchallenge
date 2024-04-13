package com.challenge.movieappchallenge.presentaion.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.challenge.movieappchallenge.presentaion.util.theme.MovieCleanTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentScreen: HomeDestination by remember { mutableStateOf(Movies) }
            val navController = rememberNavController()
            MovieCleanTheme {
                StatusBarColor()
                HomeNavHost(navController, currentScreen)
            }
        }
    }

}
