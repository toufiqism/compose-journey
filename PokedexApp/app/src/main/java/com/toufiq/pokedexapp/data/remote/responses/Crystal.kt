package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Crystal(
    @SerializedName("back_default")
    var backDefault: String?,
    @SerializedName("back_shiny")
    var backShiny: String?,
    @SerializedName("back_shiny_transparent")
    var backShinyTransparent: String?,
    @SerializedName("back_transparent")
    var backTransparent: String?,
    @SerializedName("front_default")
    var frontDefault: String?,
    @SerializedName("front_shiny")
    var frontShiny: String?,
    @SerializedName("front_shiny_transparent")
    var frontShinyTransparent: String?,
    @SerializedName("front_transparent")
    var frontTransparent: String?
)