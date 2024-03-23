package com.toufiq.composemovieapp.movielist.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MovieEntity(
    var adult: Boolean,
    var backdropPath: String,
    var genreIds: String,
    @PrimaryKey
    var id: Int,
    var originalLanguage: String,
    var originalTitle: String,
    var overview: String,
    var popularity: Double,
    var posterPath: String,
    var releaseDate: String,
    var title: String,
    var video: Boolean,
    var voteAverage: Double,
    var voteCount: Int,
    val category: String
)