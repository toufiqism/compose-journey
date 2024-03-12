package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("name")
    var name: String?,
    @SerializedName("url")
    var url: String?
)