package com.example.football.data.model.detail

import com.google.gson.annotations.SerializedName

data class DetailBaoMoiData(
    @SerializedName("data")
    val data: Data,

    @SerializedName("error_code")
    val errorCode: Int,

    @SerializedName("error_message")
    val errorMessage: String
)