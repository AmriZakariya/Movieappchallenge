package com.challenge.movieappchallenge.domain.repo

import androidx.paging.Pager
import com.challenge.movieappchallenge.data.models.MovieLocal
import com.challenge.movieappchallenge.data.models.MoviesRemoteResponse
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.presentaion.models.SortingValue

interface MoviesRepository {

    suspend fun getPopularMovies(): Pager<Int, MoviesRemoteResponse.Movie>

    suspend fun getTopRatedMovies(): Pager<Int, MoviesRemoteResponse.Movie>

    suspend fun getCachedPopularMovies(): Pager<Int, MovieLocal>

    suspend fun searchMovies(query: String): Pager<Int, MoviesRemoteResponse.Movie>

    suspend fun sortMovies(sortingValue: SortingValue): Pager<Int, MoviesRemoteResponse.Movie>

    suspend fun addFavoriteMovie(movie: Movie)

    suspend fun removeFavoriteMovie(movie: Movie)

    suspend fun getFavoriteMoviesList(): Pager<Int, MovieLocal>

}
