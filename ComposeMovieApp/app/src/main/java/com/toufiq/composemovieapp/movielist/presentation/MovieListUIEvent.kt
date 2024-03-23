package com.toufiq.composemovieapp.movielist.presentation

sealed class MovieListUIEvent {
    data class Paginate(val category:String): MovieListUIEvent()
    object Navigate:MovieListUIEvent()
}