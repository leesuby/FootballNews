package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class MediaUrl(
    @SerializedName("hls")
    val hls: Hls,

    @SerializedName("hls_h265")
    val hlsH265: HlsH265,

    @SerializedName("mp4")
    val mp4: Mp4,

    @SerializedName("mp4_h265")
    val mp4H265: Mp4H265
)