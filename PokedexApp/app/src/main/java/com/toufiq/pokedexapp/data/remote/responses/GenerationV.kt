package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    var blackWhite: BlackWhite?
)