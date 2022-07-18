package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("content")
    val content: String,

    @SerializedName("duration")
    val duration: String,

    @SerializedName("height")
    val height: Int,

    @SerializedName("media_id")
    val mediaId: String,

    @SerializedName("media_url")
    val mediaUrl: MediaUrl,

    @SerializedName("poster")
    val poster: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("width")
    val width: Int
)