package com.example.football.data.model.detail

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("content")
    val content: String,

    @SerializedName("duration")
    val duration: String = "",

    @SerializedName("height")
    val height: Int = 0,

    @SerializedName("originUrl")
    val originUrl: String?,

    @SerializedName("subtype")
    val subtype: String?,

    @SerializedName("type")
    val type: String,

    @SerializedName("width")
    val width: Int = 0
)