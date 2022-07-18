package com.example.football.data.model.detail

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("ads_topics")
    val adsTopics: Any,

    @SerializedName("content")
    val content: Content,

    @SerializedName("related")
    val related: Related
)