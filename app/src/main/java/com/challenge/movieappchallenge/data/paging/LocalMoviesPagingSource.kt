package com.challenge.movieappchallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.challenge.movieappchallenge.data.local.MoviesDao
import com.challenge.movieappchallenge.data.models.MovieLocal
import com.challenge.movieappchallenge.data.util.INITIAL_PAGE
import com.challenge.movieappchallenge.util.Logger
import java.io.IOException

private const val TAG = "MoviesPagingSource"

class LocalMoviesPagingSource(
    private val moviesDao: MoviesDao,
    private val firstPage: Int = INITIAL_PAGE,
) : PagingSource<Int, MovieLocal>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieLocal> {
        return try {
            val currentPage = params.key ?: firstPage

            Logger.i(TAG, "current_movie_page:: $currentPage")
            val moviesList = moviesDao.getMovies(
                params.loadSize,
                (currentPage - 1) * params.loadSize
            )

            val nextPage: Int? =
                if (moviesList.isEmpty()) null else currentPage.plus(1)

            if (moviesList.isEmpty()) {
                throw IOException()
            }

            LoadResult.Page(
                data = moviesList,
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieLocal>): Int {
        return 0
    }

}
