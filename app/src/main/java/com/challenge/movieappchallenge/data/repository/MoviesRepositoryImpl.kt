package com.challenge.movieappchallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.challenge.movieappchallenge.data.local.MoviesDao
import com.challenge.movieappchallenge.data.models.MovieLocal
import com.challenge.movieappchallenge.data.models.MoviesRemoteResponse
import com.challenge.movieappchallenge.data.paging.LocalMoviesPagingSource
import com.challenge.movieappchallenge.data.paging.MoviesType
import com.challenge.movieappchallenge.data.paging.RemoteMoviesPagingSource
import com.challenge.movieappchallenge.data.util.PAGE_SIZE_PAGING_LOCAL_MOVIE
import com.challenge.movieappchallenge.data.util.PAGE_SIZE_PAGING_REMOTE_MOVIE
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.domain.repo.MoviesRepository
import com.challenge.movieappchallenge.presentaion.models.SortingValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MoviesRepositoryImpl @Inject constructor(
    private val moviesLocalPagingSource: LocalMoviesPagingSource,
    private val remoteMoviesPagingSource: RemoteMoviesPagingSource,
    private val moviesDao: MoviesDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MoviesRepository {

    override suspend fun getPopularMovies(): Pager<Int, MoviesRemoteResponse.Movie> =
        withContext(dispatcher) {
            // change movies type
            remoteMoviesPagingSource.setMoviesType(MoviesType.POPULAR)
            remoteMoviesPagingSource.setForceCaching(true)
            // get from API
            return@withContext Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_REMOTE_MOVIE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                remoteMoviesPagingSource
            })
        }

    override suspend fun getTopRatedMovies(): Pager<Int, MoviesRemoteResponse.Movie> =
        withContext(dispatcher) {
            remoteMoviesPagingSource.setMoviesType(MoviesType.TOP_RATED)
            remoteMoviesPagingSource.setForceCaching(false)
            return@withContext Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_REMOTE_MOVIE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                remoteMoviesPagingSource
            })
        }

    override suspend fun getCachedPopularMovies(): Pager<Int, MovieLocal> =
        withContext(dispatcher) {
            return@withContext Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_LOCAL_MOVIE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                moviesLocalPagingSource
            })
        }

    override suspend fun searchMovies(query: String): Pager<Int, MoviesRemoteResponse.Movie> =
        withContext(dispatcher) {
            remoteMoviesPagingSource.setSearchQuery(query)
            remoteMoviesPagingSource.setMoviesType(MoviesType.SEARCH)
            remoteMoviesPagingSource.setForceCaching(false)
            return@withContext Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_REMOTE_MOVIE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                remoteMoviesPagingSource
            })
        }

    override suspend fun sortMovies(sortingValue: SortingValue): Pager<Int, MoviesRemoteResponse.Movie> =
        withContext(dispatcher) {
            remoteMoviesPagingSource.setSortingValue(sortingValue)
            remoteMoviesPagingSource.setForceCaching(false)
            return@withContext Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_REMOTE_MOVIE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                remoteMoviesPagingSource
            })
        }

    override suspend fun addFavoriteMovie(movie: Movie) {
        moviesDao.addFavorite(movie.toLocalFavoriteMovie())
    }

    override suspend fun removeFavoriteMovie(movie: Movie) {
        moviesDao.removeFavorite(movie.toLocalFavoriteMovie())
    }

    override suspend fun getFavoriteMoviesList(): Pager<Int, MovieLocal> =
        withContext(dispatcher) {
            remoteMoviesPagingSource.setMoviesType(MoviesType.FAVORITE)
            return@withContext Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_LOCAL_MOVIE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                moviesLocalPagingSource
            })
        }
}
