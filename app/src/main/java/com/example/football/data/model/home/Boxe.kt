package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class Boxe(
    @SerializedName("description")
    val description: String,

    @SerializedName("display_type")
    val displayType: Int,

    @SerializedName("max_show")
    val maxShow: Int,

    @SerializedName("object_type")
    val objectType: Int,

    @SerializedName("position")
    val position: Int,

    @SerializedName("positions")
    val positions: List<Int>,

    @SerializedName("section_box_id")
    val sectionBoxId: Int,

    @SerializedName("segment_ids")
    val segmentIds: List<Any>,

    @SerializedName("soccer_competitions")
    val soccerCompetitions: List<SoccerCompetition>,

    @SerializedName("soccer_matches")
    val soccerMatches: List<SoccerMatch>,

    @SerializedName("title")
    val title: String,

    @SerializedName("videos")
    val videos: List<Video>,

    @SerializedName("zone")
    val zone: String
)