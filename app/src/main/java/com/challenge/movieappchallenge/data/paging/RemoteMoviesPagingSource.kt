package com.challenge.movieappchallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.challenge.movieappchallenge.data.local.MoviesDao
import com.challenge.movieappchallenge.data.mappers.movie.toLocalMovie
import com.challenge.movieappchallenge.data.models.MoviesRemoteResponse
import com.challenge.movieappchallenge.data.remote.RetrofitApi
import com.challenge.movieappchallenge.data.util.INITIAL_PAGE
import com.challenge.movieappchallenge.data.util.cachedPages
import com.challenge.movieappchallenge.util.Logger

private const val TAG = "MoviesPagingSource"

enum class MoviesType {
    POPULAR,
    TOP_RATED,
    SEARCH,
    NONE
}

class RemoteMoviesPagingSource(
    private val retrofitApi: RetrofitApi,
    private val moviesDao: MoviesDao,
    private val firstPage: Int = INITIAL_PAGE,
    private var moviesType: MoviesType = MoviesType.NONE,
    private var forceCashing: Boolean = false,
    private val cachingPagesNum: List<Int> = cachedPages,
    private var searchQuery: String = ""
) : PagingSource<Int, MoviesRemoteResponse.Movie>() {

    fun setForceCaching(isCache: Boolean) {
        forceCashing = isCache
    }

    fun setMoviesType(moviesType: MoviesType) {
        this.moviesType = moviesType
    }

    fun setSearchQuery(query: String) {
        this.searchQuery = query
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesRemoteResponse.Movie> {
        return try {
            val currentPage = params.key ?: firstPage

            Logger.i(TAG, "current_movie_page:: $currentPage")
            val moviesList = when (moviesType) {
                MoviesType.POPULAR -> retrofitApi.getPopularMovies(currentPage).movies
                MoviesType.TOP_RATED -> retrofitApi.getTopRatedMovies(currentPage).movies
                MoviesType.SEARCH -> retrofitApi.searchMovies(searchQuery, currentPage).movies
                MoviesType.NONE -> {
                    throw IllegalArgumentException(
                        "Parameter MoviesType Not passed",
                        Throwable("Please select movies type to get")
                    )
                }
            } ?: emptyList()

            if (forceCashing) {
                if (moviesList.isNotEmpty() && currentPage in cachingPagesNum) {
                    val localMovies = moviesList.map {
                        it.toLocalMovie()
                    }
                    moviesDao.insertMovies(localMovies)
                }
            }

            val nextPage: Int? =
                if (moviesList.isEmpty()) null else currentPage.plus(1)

            LoadResult.Page(
                data = moviesList,
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesRemoteResponse.Movie>): Int {
        return 0
    }

}
