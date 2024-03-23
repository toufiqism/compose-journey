package com.toufiq.composemovieapp.di

import com.toufiq.composemovieapp.movielist.data.repository.MovieListRepositoryImpl
import com.toufiq.composemovieapp.movielist.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieListRepository(movieListRepositoryImpl: MovieListRepositoryImpl):MovieListRepository
}