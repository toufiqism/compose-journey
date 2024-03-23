package com.toufiq.composemovieapp.movielist.domain.repository

import com.toufiq.composemovieapp.movielist.domain.model.Movie
import com.toufiq.composemovieapp.movielist.utils.Resource
import kotlinx.coroutines.flow.Flow


interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>
}