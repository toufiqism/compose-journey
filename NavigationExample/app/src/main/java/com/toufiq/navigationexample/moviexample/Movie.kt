package com.toufiq.navigationexample.moviexample

import androidx.annotation.DrawableRes

data class Movie(
    val id: Long,
    val name: String,
    val category: String,
    val favorite: Boolean = false,
    @DrawableRes val image: Int
)