package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class PokemonList(
    @SerializedName("count")
    var count: Int?,
    @SerializedName("next")
    var next: String?,
    @SerializedName("previous")
    var previous: Any?,
    @SerializedName("results")
    var results: List<Result>
)