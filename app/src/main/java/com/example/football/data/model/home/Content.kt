package com.example.football.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.football.data.model.detail.Image

data class Content constructor(
    val content_id: Int,
    val attributes: Int = 0,
    val avatar_height: Int = 0,
    val avatar_url: String?="",
    val avatar_width: Int = 0,
    val category_id: Int = 0,
    val category_name: String="",
    val category_zone: String="",
    val cluster_id: Int= 0,
    val comment_count: Int=0,
    val date: Int,
    val description: String="",
    val modified_date: Int=0,
    val original_url: String="",
    val publisher_has_info: Boolean= true,
    val publisher_icon: String="",
    val publisher_id: Int=0,
    val publisher_logo: String?="",
    val publisher_name: String="",
    val publisher_zone: String="",
    val redirect_url: String="",
    val server_index: Int=0,
    val source_name: String="",
    val title: String,
    val url: String="",
    val images: List<Image>? = null,
    var bitmapAvatar: Bitmap? = null
)