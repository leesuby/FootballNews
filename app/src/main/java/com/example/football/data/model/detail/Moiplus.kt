package com.example.football.data.model.detail

import com.google.gson.annotations.SerializedName

data class Moiplus(
    @SerializedName("comment_count")
    val commentCount: Int,

    @SerializedName("scheme")
    val scheme: String,

    @SerializedName("url")
    val url: String
)