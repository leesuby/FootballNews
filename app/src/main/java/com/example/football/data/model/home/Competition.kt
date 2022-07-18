package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class Competition(
    @SerializedName("competition_id")
    val competitionId: Int,

    @SerializedName("competition_logo")
    val competitionLogo: String,

    @SerializedName("competition_name")
    val competitionName: String,

    @SerializedName("country_id")
    val countryId: Int,

    @SerializedName("country_name")
    val countryName: Any,

    @SerializedName("zone")
    val zone: String
)