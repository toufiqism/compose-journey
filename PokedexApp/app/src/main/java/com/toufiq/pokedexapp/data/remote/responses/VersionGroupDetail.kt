package com.toufiq.pokedexapp.data.remote.responses


import com.google.gson.annotations.SerializedName

data class VersionGroupDetail(
    @SerializedName("level_learned_at")
    var levelLearnedAt: Int?,
    @SerializedName("move_learn_method")
    var moveLearnMethod: MoveLearnMethod?,
    @SerializedName("version_group")
    var versionGroup: VersionGroup?
)