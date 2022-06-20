package com.example.football.database.model.detail

import com.example.football.database.model.Content

data class Related(
    val contents: List<Content>,
    val title: String
)