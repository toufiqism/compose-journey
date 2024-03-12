package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class VersionDetail(
    @SerializedName("rarity")
    var rarity: Int?,
    @SerializedName("version")
    var version: Version?
)