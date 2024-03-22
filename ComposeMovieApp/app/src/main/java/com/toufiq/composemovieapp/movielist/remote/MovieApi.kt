package com.toufiq.composemovieapp.movielist.remote

import retrofit2.http.GET

interface MovieApi {

    @GET
    suspend fun getMoviesList()
}