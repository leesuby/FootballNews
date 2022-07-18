package com.example.football.data.model

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("attributes")
    val attributes: Int,

    @SerializedName("avartar_height")
    val avatarHeight: Int,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("avatar_width")
    val avatarWidth: Int,

    @SerializedName("body")
    val body: List<Body>,

    @SerializedName("category_id")
    val categoryId: Int,

    @SerializedName("category_name")
    val categoryName: String,

    @SerializedName("category_zone")
    val categoryZone: String,

    @SerializedName("cluster_id")
    val clusterId: Int,

    @SerializedName("comment_count")
    val commentCount: Int,

    @SerializedName("date")
    val date: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("original_url")
    val originalUrl: String,

    @SerializedName("publisher_has_info")
    val publisherHasInfo: Boolean,

    @SerializedName("publisher_icon")
    val publisherIcon: String,

    @SerializedName("publisher_id")
    val publisherId: Int,

    @SerializedName("publisher_logo")
    val publisherLogo: String,

    @SerializedName("publisher_name")
    val publisherName: String,

    @SerializedName("publisher_zone")
    val publisherZone: String,

    @SerializedName("server_index")
    val serverIndex: Int,

    @SerializedName("source_name")
    val sourceName: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("video_id")
    val videoId: Int
)