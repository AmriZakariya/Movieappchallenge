package com.challenge.movieappchallenge.data.remote

import com.challenge.movieappchallenge.data.models.MoviesRemoteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String? = null,
        @Query("include_adult") includeAdult: Boolean = false
    ): MoviesRemoteResponse

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String? = null,
        @Query("include_adult") includeAdult: Boolean = false
    ): MoviesRemoteResponse

    @GET("/3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String? = null,
        @Query("include_adult") includeAdult: Boolean = false
    ): MoviesRemoteResponse
}
