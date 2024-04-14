package com.challenge.movieappchallenge.presentaion.ui

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.presentaion.viewModel.MoviesViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FavoritesListScreen(
    moviesViewModel: MoviesViewModel,
    onMovieClicked: (Movie) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
        content = {
            it.toString()
            MoviesContent(moviesViewModel.moviesList, onMovieClicked, onRefresh = {
                moviesViewModel.refreshData.trySend(true)
            })
        })
}

