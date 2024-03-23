package com.toufiq.composemovieapp.core.routes

sealed class Routes(val rout: String) {
    object Home : Routes("main")
    object PopularMovieList : Routes("popularMovie")
    object UpcomingMovieList : Routes("upcomingMovie")
    object Details : Routes("details")
}