package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Cries(
    @SerializedName("latest")
    var latest: String?,
    @SerializedName("legacy")
    var legacy: String?
)