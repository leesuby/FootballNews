package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class Image(

    @SerializedName("content_id")
    val contentId: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("url")
    val url: String,

    @SerializedName("width")
    val width: Int
)