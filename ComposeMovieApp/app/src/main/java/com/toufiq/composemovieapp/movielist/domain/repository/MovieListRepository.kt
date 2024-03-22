package com.toufiq.composemovieapp.movielist.domain.repository

import com.toufiq.composemovieapp.movielist.domain.model.Movie
import com.toufiq.composemovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow


interface MovieListRepository {
    suspend fun getMovieList(forceFetchFromRemote:Boolean,category:String,page:Int): Flow<Resource<List<Movie>>>
}