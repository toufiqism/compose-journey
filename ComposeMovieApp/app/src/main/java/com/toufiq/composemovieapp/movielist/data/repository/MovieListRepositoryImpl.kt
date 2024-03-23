package com.toufiq.composemovieapp.movielist.data.repository

import com.toufiq.composemovieapp.movielist.data.local.movie.MovieDatabase
import com.toufiq.composemovieapp.movielist.data.local.movie.toMovie
import com.toufiq.composemovieapp.movielist.data.local.movie.toMovieEntity
import com.toufiq.composemovieapp.movielist.data.remote.MovieApi
import com.toufiq.composemovieapp.movielist.domain.model.Movie
import com.toufiq.composemovieapp.movielist.domain.repository.MovieListRepository
import com.toufiq.composemovieapp.movielist.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
            val shouldLoadLocal = localMovieList.isNotEmpty() && !forceFetchFromRemote
            if (shouldLoadLocal) {
                emit(Resource.Success(data = localMovieList.map { movieEntity ->
                    movieEntity.toMovie(
                        category
                    )
                }))
                emit(Resource.Loading(false))
                return@flow
            }
            val movieListFromApi = try {
                api.getMoviesList(category, page)
            } catch (exc: Exception) {
                emit(Resource.Error(message = "Error Loading Movies: " + exc.stackTraceToString()))
                return@flow
            }
            val movieEntities = movieListFromApi.results!!.let {
                it.map { movieDto -> movieDto.toMovieEntity(category) }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(
                Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity = movieDatabase.movieDao.getMovieById(id)
            if (movieEntity != null) {
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such movie"))
            emit(Resource.Loading(false))
        }
    }
}