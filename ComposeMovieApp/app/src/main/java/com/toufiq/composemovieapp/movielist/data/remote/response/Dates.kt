package com.toufiq.composemovieapp.movielist.data.remote.response


import com.google.gson.annotations.SerializedName

data class Dates(
    @SerializedName("maximum")
    var maximum: String?,
    @SerializedName("minimum")
    var minimum: String?
)