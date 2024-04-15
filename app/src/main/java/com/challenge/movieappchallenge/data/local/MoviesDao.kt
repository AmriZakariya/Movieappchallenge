package com.challenge.movieappchallenge.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.challenge.movieappchallenge.data.models.MovieLocal

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getMovies(limit: Int, offset: Int): List<MovieLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(movieLocal: MovieLocal)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun removeFavorite(movieLocal: MovieLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieLocal>)

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

}
