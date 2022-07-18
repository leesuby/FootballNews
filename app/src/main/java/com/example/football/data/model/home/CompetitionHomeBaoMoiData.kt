package com.example.football.data.model.home

import com.google.gson.annotations.SerializedName

data class CompetitionHomeBaoMoiData(
    @SerializedName("data")
    val `data`: CompetitionData,

    @SerializedName("error_code")
    val errorCode: Int,

    @SerializedName("error_message")
    val errorMessage: String
)