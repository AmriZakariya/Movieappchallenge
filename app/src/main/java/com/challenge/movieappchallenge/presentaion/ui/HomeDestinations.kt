package com.challenge.movieappchallenge.presentaion.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


interface HomeDestination {
    val route: String
    val icon: ImageVector
    val title: String
    val screen: @Composable () -> Unit
}

object MoviesDestination : HomeDestination {
    override val route = "Movies"
    override val icon: ImageVector = Icons.Filled.Home
    override val title = "Movies"
    override val screen: @Composable () -> Unit = { }
}

object MovieDetails : HomeDestination {
    override val route = "MovieDetails"
    override val icon: ImageVector = Icons.Default.Info
    override val title = "Movie Details"
    override val screen: @Composable () -> Unit = { }
}

object FavoritesDestination : HomeDestination {
    override val route = "Favorites"
    override val icon: ImageVector = Icons.Filled.Favorite
    override val title = "Favorites"
    override val screen: @Composable () -> Unit = { }
}

val homeScreens = listOf(MoviesDestination, MovieDetails)
