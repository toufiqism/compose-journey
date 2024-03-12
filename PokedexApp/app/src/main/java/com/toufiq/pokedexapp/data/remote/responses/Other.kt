package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Other(
    @SerializedName("dream_world")
    var dreamWorld: DreamWorld?,
    @SerializedName("home")
    var home: Home?,
    @SerializedName("official-artwork")
    var officialArtwork: OfficialArtwork?,
    @SerializedName("showdown")
    var showdown: Showdown?
)