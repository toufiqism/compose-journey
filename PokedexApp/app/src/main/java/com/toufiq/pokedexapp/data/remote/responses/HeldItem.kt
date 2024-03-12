package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class HeldItem(
    @SerializedName("item")
    var item: Item?,
    @SerializedName("version_details")
    var versionDetails: List<VersionDetail>?
)