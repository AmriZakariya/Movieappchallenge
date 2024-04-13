package com.challenge.movieappchallenge.presentaion.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.presentaion.viewModel.MoviesViewModel

private const val KEY_MOVIE = "movie"

@Composable
fun HomeNavHost(navController: NavHostController, currentScreen: HomeDestination) {
    NavHost(
        navController = navController,
        startDestination = Movies.route,
    ) {
        composable(route = Movies.route) {
            val moviesViewModel = hiltViewModel<MoviesViewModel>()
            MoviesScreen(moviesViewModel, onMovieClicked = { movie ->
                // Open Movie Details screen for clicked one
                navController.currentBackStackEntry?.savedStateHandle?.set(KEY_MOVIE, movie)
                navController.navigate(MovieDetails.route)
            })
        }
        composable(route = MovieDetails.route) {
            var movie by rememberSaveable {
                mutableStateOf(Movie(id = -1, name = "Not found"))
            }
            LaunchedEffect(key1 = it) {
                movie = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Movie>(KEY_MOVIE) ?: Movie(id = -1, name = "Not found")

            }
            MovieDetailsScreen(movie)

        }
    }

}
