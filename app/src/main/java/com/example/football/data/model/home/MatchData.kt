package com.example.football.data.model.home

import com.example.football.data.model.SoccerMatch
import com.google.gson.annotations.SerializedName

data class MatchData(
    @SerializedName("soccer_match")
    val soccerMatch: List<SoccerMatch>
)