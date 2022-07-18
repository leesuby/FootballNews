package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("boxes")
    val boxes: List<Boxe>?,

    @SerializedName("contents")
    var contents: List<Content>
)