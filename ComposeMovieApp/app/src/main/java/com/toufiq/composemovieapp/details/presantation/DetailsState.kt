package com.toufiq.composemovieapp.details.presantation

import com.toufiq.composemovieapp.movielist.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
