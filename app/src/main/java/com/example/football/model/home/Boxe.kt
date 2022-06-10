package com.example.football.model

data class Boxe(
    val description: String,
    val display_type: Int,
    val max_show: Int,
    val object_type: Int,
    val position: Int,
    val positions: List<Int>,
    val section_box_id: Int,
    val segment_ids: List<Any>,
    val soccer_competitions: List<SoccerCompetition>,
    val soccer_matches: List<SoccerMatche>,
    val title: String,
    val videos: List<Video>,
    val zone: String
)