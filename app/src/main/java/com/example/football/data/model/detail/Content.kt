package com.example.football.data.model.detail

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("content_id")
    val contentId: Int,

    @SerializedName("attributes")
    val attributes: Int = 0,

    @SerializedName("avatar_height")
    val avatarHeight: Int = 0,

    @SerializedName("avatar_url")
    val avatarUrl: String?="",

    @SerializedName("avatar_width")
    val avatarWidth: Int = 0,

    @SerializedName("category_id")
    val categoryId: Int = 0,

    @SerializedName("category_name")
    val categoryName: String="",

    @SerializedName("category_zone")
    val categoryZone: String="",

    @SerializedName("cluster_id")
    val clusterId: Int= 0,

    @SerializedName("comment_count")
    val commentCount: Int=0,

    @SerializedName("date")
    val date: Int,

    @SerializedName("description")
    val description: String="",

    @SerializedName("modified_date")
    val modifiedDate: Int=0,

    @SerializedName("original_url")
    val originalUrl: String="",

    @SerializedName("publisher_has_info")
    val publisherHasInfo: Boolean= true,

    @SerializedName("publisher_icon")
    val publisherIcon: String="",

    @SerializedName("publisher_id")
    val publisherId: Int=0,

    @SerializedName("publisher_logo")
    val publisherLogo: String?="",

    @SerializedName("publisher_name")
    val publisherName: String="",

    @SerializedName("publisher_zone")
    val publisherZone: String="",

    @SerializedName("redirect_url")
    val redirectUrl: String="",

    @SerializedName("server_index")
    val serverIndex: Int=0,

    @SerializedName("source_name")
    val sourceName: String="",

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String="",

    @SerializedName("images")
    val images: List<Image>? = null,

    @SerializedName("body")
    val body: List<Body>,



)