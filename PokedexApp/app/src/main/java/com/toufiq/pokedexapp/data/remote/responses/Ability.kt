package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Ability(
    @SerializedName("ability")
    var ability: AbilityX?,
    @SerializedName("is_hidden")
    var isHidden: Boolean?,
    @SerializedName("slot")
    var slot: Int?
)