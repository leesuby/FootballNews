package com.example.football.database.model

data class Video(
    val attributes: Int,
    val avatar_height: Int,
    val avatar_url: String,
    val avatar_width: Int,
    val body: List<Body>,
    val category_id: Int,
    val category_name: String,
    val category_zone: String,
    val cluster_id: Int,
    val comment_count: Int,
    val date: Int,
    val description: String,
    val original_url: String,
    val publisher_has_info: Boolean,
    val publisher_icon: String,
    val publisher_id: Int,
    val publisher_logo: String,
    val publisher_name: String,
    val publisher_zone: String,
    val server_index: Int,
    val source_name: String,
    val title: String,
    val url: String,
    val video_id: Int
)