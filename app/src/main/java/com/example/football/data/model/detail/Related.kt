package com.example.football.data.model.detail

import com.example.football.data.model.Content

data class Related(
    val contents: List<Content>,
    val title: String = "Tin khác"
)