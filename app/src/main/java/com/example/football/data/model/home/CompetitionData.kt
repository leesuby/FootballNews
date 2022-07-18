package com.example.football.data.model.home

import com.example.football.data.model.SoccerCompetition
import com.google.gson.annotations.SerializedName

data class CompetitionData(
    @SerializedName("soccer_competitions")
    val soccerCompetitions: List<SoccerCompetition>
)