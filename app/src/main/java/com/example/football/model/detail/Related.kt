package com.example.football.model.detail

import com.example.football.model.Content

data class Related(
    val contents: List<Content>,
    val title: String
)