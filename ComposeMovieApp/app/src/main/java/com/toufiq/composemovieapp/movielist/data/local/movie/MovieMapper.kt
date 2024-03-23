package com.toufiq.composemovieapp.movielist.data.local.movie


import com.toufiq.composemovieapp.movielist.data.remote.response.MovieDto
import com.toufiq.composemovieapp.movielist.domain.model.Movie
import com.toufiq.composemovieapp.movielist.data.local.movie.MovieEntity

/**
 * @author Android Devs Academy (Ahmed Guedmioui)
 */


fun MovieDto.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdropPath = backdrop_path ?: "",
        originalLanguage = original_language ?: "",
        overview = overview ?: "",
        posterPath = poster_path ?: "",
        releaseDate = release_date ?: "",
        title = title ?: "",
        voteAverage = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        voteCount = vote_count ?: 0,
        id = id ?: -1,
        originalTitle = original_title ?: "",
        video = video ?: false,

        category = category,

        genreIds = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        }
    )
}

fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        backdrop_path = backdropPath,
        original_language = originalLanguage,
        overview = overview,
        poster_path = posterPath,
        release_date = releaseDate,
        title = title,
        vote_average = voteAverage,
        popularity = popularity,
        vote_count = voteCount,
        video = video,
        id = id,
        adult = adult,
        original_title = originalTitle,

        category = category,

        genre_ids = try {
            genreIds.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        }
    )
}




















