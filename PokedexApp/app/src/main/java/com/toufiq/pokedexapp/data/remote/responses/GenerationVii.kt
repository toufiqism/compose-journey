package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class GenerationVii(
    @SerializedName("icons")
    var icons: Icons?,
    @SerializedName("ultra-sun-ultra-moon")
    var ultraSunUltraMoon: UltraSunUltraMoon?
)