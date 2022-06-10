package com.example.football.model

data class Competition(
    val competition_id: Int,
    val competition_logo: String,
    val competition_name: String,
    val country_id: Int,
    val country_name: Any,
    val zone: String
)