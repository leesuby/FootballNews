package com.example.football.model

data class SoccerMatche(
    val away_scored: Int,
    val away_scored_note: String,
    val away_team: AwayTeam,
    val competition: Competition,
    val group_id: Int,
    val group_name: String,
    val home_scored: Int,
    val home_scored_note: String,
    val home_team: HomeTeam,
    val match_id: Int,
    val match_status: Int,
    val round: String,
    val start_time: String,
    val time: String,
    val zone: String
)