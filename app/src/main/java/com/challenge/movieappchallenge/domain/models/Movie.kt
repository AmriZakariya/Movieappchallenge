package com.challenge.movieappchallenge.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.challenge.movieappchallenge.data.models.MovieLocal
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Movie(
    val id: Int = 0,
    val movieId: Int = 0,
    val name: String = "",
    val imageUrlSmall: String = "",
    val imageUrlFull: String = "",
    val description: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val overview: String = "",
    val releaseDate: String = "",
) : Parcelable {
    fun toLocalFavoriteMovie(): MovieLocal {
        return MovieLocal(
            id = id,
            movieId = movieId,
            name = name,
            imageUrlSmall = imageUrlSmall,
            imageUrlFull = imageUrlFull,
            description = description,
            voteAverage = voteAverage,
            voteCount = voteCount,
            overview = overview,
            releaseDate = releaseDate,
            isFavorite = true
        )
    }
}
