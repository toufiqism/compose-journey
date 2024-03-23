package com.toufiq.composemovieapp.movielist.data.remote.response


import com.google.gson.annotations.SerializedName

data class MovieListDto(
    @SerializedName("dates")
    var dates: Dates?,
    @SerializedName("page")
    var page: Int?,
    @SerializedName("results")
    var results: List<MovieDto>?,
    @SerializedName("total_pages")
    var totalPages: Int?,
    @SerializedName("total_results")
    var totalResults: Int?
)