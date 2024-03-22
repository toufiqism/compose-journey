package com.toufiq.composemovieapp.movielist.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase
import com.toufiq.composemovieapp.movielist.domain.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}