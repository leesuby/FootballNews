package com.example.football.database.model

data class Body(
    val content: String,
    val duration: String,
    val height: Int,
    val media_id: String,
    val media_url: MediaUrl,
    val poster: String,
    val type: String,
    val width: Int
)