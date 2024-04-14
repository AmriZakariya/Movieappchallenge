package com.challenge.movieappchallenge.presentaion.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.challenge.movieappchallenge.presentaion.util.theme.MovieAppChallengeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                MoviesDestination,
                FavoritesDestination
            )
            val currentScreen: MutableState<HomeDestination> = remember {
                mutableStateOf(MoviesDestination)
            }

            MovieAppChallengeTheme {
                StatusBarColor()
                Scaffold(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background),
                    bottomBar = {
                        BottomNavigation {
                            items.forEach { screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(screen.title) },
                                    selected = currentScreen.value == screen,
                                    onClick = {
                                        currentScreen.value = screen
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) {
                    it.toString()
                    StatusBarColor()
                    HomeNavHost(navController, currentScreen.value)
                }
            }

        }
    }
}
