package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class SoccerMatch(

    @SerializedName("away_scored")
    val awayScored: Int,

    @SerializedName("away_scored_note")
    val awayScoredNote: String,

    @SerializedName("away_team")
    val awayTeam: AwayTeam,

    @SerializedName("competition")
    val competition: Competition,

    @SerializedName("group_id")
    val groupId: Int,

    @SerializedName("group_name")
    val groupName: String,

    @SerializedName("home_scored")
    val homeScored: Int,

    @SerializedName("home_scored_note")
    val homeScoredNote: String,

    @SerializedName("home_team")
    val homeTeam: HomeTeam,

    @SerializedName("match_id")
    val matchId: Int,

    @SerializedName("match_status")
    val matchStatus: Int,

    @SerializedName("round")
    val round: String,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("zone")
    val zone: String
)