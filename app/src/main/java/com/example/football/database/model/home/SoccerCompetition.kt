package com.example.football.database.model

data class SoccerCompetition(
    val competition_id: Int,
    val competition_logo: String,
    val competition_name: String,
    val country_id: Int,
    val country_name: String,
    val zone: String
)