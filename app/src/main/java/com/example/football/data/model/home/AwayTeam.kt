package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class AwayTeam(
    @SerializedName("team_id")
    val teamId: Int,

    @SerializedName("team_logo")
    val teamLogo: String,

    @SerializedName("team_name")
    val teamName: String,

    @SerializedName("zone")
    val zone: String
)