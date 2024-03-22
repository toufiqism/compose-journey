package com.toufiq.composemovieapp.movielist.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.toufiq.composemovieapp.movielist.domain.model.MovieEntity


@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity where id = :id")
    suspend fun getMovieById(id: Int): MovieEntity


    @Query("SELECT * FROM MovieEntity where category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
}