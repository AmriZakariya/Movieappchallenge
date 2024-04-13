package com.challenge.movieappchallenge.domain.useCase

import androidx.paging.PagingData
import com.challenge.movieappchallenge.domain.models.Movie
import kotlinx.coroutines.flow.Flow


interface MovieUseCase {

    suspend fun getTopRatedMovies(): Flow<PagingData<Movie>>

    suspend fun getMostPopularMovies(): Flow<PagingData<Movie>>
}
