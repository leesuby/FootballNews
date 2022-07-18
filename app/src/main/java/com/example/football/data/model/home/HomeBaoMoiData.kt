package com.example.football.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeBaoMoiData(
    @SerializedName("data")
    val data: Data,

    @SerializedName("error_code")
    val errorCode: Int,

    @SerializedName("error_message")
    val errorMessage: String
) : Serializable