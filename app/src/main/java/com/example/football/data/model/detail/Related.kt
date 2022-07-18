package com.example.football.data.model.detail

import com.example.football.data.model.Content
import com.google.gson.annotations.SerializedName

data class Related(

    @SerializedName("contents")
    val contents: List<Content>,

    @SerializedName("title")
    val title: String = "Tin khác"
)