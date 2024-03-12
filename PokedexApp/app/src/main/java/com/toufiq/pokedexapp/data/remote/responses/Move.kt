package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Move(
    @SerializedName("move")
    var move: MoveX?,
    @SerializedName("version_group_details")
    var versionGroupDetails: List<VersionGroupDetail>?
)