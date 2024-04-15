package com.challenge.movieappchallenge.domain.useCase

import androidx.paging.PagingData
import androidx.paging.map
import com.challenge.movieappchallenge.data.mappers.movie.toMovie
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.domain.repo.MoviesRepository
import com.challenge.movieappchallenge.presentaion.models.SortingValue
import com.challenge.movieappchallenge.util.Logger
import com.challenge.movieappchallenge.util.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MoviesUseCaseImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val networkState: NetworkState = NetworkState
) :
    MovieUseCase {

    private val TAG = "MoviesUseCaseImpl"

    override suspend fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return moviesRepository.getTopRatedMovies().flow.map { moviesList ->
            moviesList.map { movie ->
                movie.toMovie()
            }
        }.distinctUntilChanged()
    }

    override suspend fun getMostPopularMovies(): Flow<PagingData<Movie>> {
        Logger.i(TAG, networkState.isOnline().toString())
        val movies = if (networkState.isOnline()) {
            moviesRepository.getPopularMovies().flow.map { moviesList ->
                moviesList.map { movie ->
                    movie.toMovie()
                }
            }
        } else {
            moviesRepository.getCachedPopularMovies().flow.map { moviesList ->
                moviesList.map { movie ->
                    movie.toMovie()
                }
            }
        }
        return movies.distinctUntilChanged()
    }

    override suspend fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return moviesRepository.searchMovies(query).flow.map { moviesList ->
            moviesList.map { movie ->
                movie.toMovie()
            }
        }.distinctUntilChanged()
    }

    override suspend fun sortMovies(sortingValue: SortingValue): Flow<PagingData<Movie>> {
        return moviesRepository.sortMovies(sortingValue).flow.map { moviesList ->
            moviesList.map { movie ->
                movie.toMovie()
            }
        }.distinctUntilChanged()
    }

    override suspend fun addFavoriteMovie(movie: Movie) {
        moviesRepository.addFavoriteMovie(movie)
    }


    override suspend fun getFavoriteMoviesList(): Flow<PagingData<Movie>> {
        Logger.i(TAG, networkState.isOnline().toString())
        return moviesRepository.getFavoriteMoviesList().flow.map { moviesList ->
            moviesList.map { movie ->
                movie.toMovie()
            }
        }.distinctUntilChanged()
    }
}
