package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("base_stat")
    var baseStat: Int?,
    @SerializedName("effort")
    var effort: Int?,
    @SerializedName("stat")
    var stat: StatX?
)