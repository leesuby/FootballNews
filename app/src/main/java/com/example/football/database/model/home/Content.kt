package com.example.football.database.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "ContentHome")
data class Content(
    @PrimaryKey
    val content_id: Int,
    val attributes: Int,
    val avatar_height: Int,
    val avatar_url: String,
    val avatar_width: Int,
    val category_id: Int,
    val category_name: String,
    val category_zone: String,
    val cluster_id: Int,
    val comment_count: Int,
    val date: Int,
    val description: String,
    val modified_date: Int,
    val original_url: String,
    val publisher_has_info: Boolean,
    val publisher_icon: String,
    val publisher_id: Int,
    val publisher_logo: String,
    val publisher_name: String,
    val publisher_zone: String,
    val redirect_url: String,
    val server_index: Int,
    val source_name: String,
    val title: String,
    val url: String
)