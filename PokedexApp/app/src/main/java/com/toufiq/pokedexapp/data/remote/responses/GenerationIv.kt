package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class GenerationIv(
    @SerializedName("diamond-pearl")
    var diamondPearl: DiamondPearl?,
    @SerializedName("heartgold-soulsilver")
    var heartgoldSoulsilver: HeartgoldSoulsilver?,
    @SerializedName("platinum")
    var platinum: Platinum?
)