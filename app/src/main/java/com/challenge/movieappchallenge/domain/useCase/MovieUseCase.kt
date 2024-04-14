package com.challenge.movieappchallenge.domain.useCase

import androidx.paging.PagingData
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.presentaion.models.SortingValue
import kotlinx.coroutines.flow.Flow


interface MovieUseCase {

    suspend fun getTopRatedMovies(): Flow<PagingData<Movie>>

    suspend fun getMostPopularMovies(): Flow<PagingData<Movie>>

    suspend fun searchMovies(query: String): Flow<PagingData<Movie>>

    suspend fun sortMovies(sortingValue: SortingValue): Flow<PagingData<Movie>>
}
