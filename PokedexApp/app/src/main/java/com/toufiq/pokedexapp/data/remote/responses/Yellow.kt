package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Yellow(
    @SerializedName("back_default")
    var backDefault: String?,
    @SerializedName("back_gray")
    var backGray: String?,
    @SerializedName("back_transparent")
    var backTransparent: String?,
    @SerializedName("front_default")
    var frontDefault: String?,
    @SerializedName("front_gray")
    var frontGray: String?,
    @SerializedName("front_transparent")
    var frontTransparent: String?
)