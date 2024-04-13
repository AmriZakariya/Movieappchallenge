package com.challenge.movieappchallenge.domain.repo

import androidx.paging.Pager
import com.challenge.movieappchallenge.data.models.MovieLocal
import com.challenge.movieappchallenge.data.models.MoviesRemoteResponse

interface MoviesRepository {

    suspend fun getPopularMovies(): Pager<Int, MoviesRemoteResponse.Movie>

    suspend fun getTopRatedMovies(): Pager<Int, MoviesRemoteResponse.Movie>

    suspend fun getCachedPopularMovies(): Pager<Int, MovieLocal>
}
