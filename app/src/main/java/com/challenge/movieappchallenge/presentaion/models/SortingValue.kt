package com.challenge.movieappchallenge.presentaion.models

import androidx.annotation.Keep

@Keep
enum class SortingValue(val sortByValue: String) {
    ALPHABETICAL("title.asc"),
    DATE("primary_release_date.asc");
}
