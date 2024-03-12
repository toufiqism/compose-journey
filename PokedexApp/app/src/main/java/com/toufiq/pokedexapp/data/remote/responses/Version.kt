package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("name")
    var name: String?,
    @SerializedName("url")
    var url: String?
)