package com.example.football.data.local.database.home

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.football.data.model.Content

@Entity(tableName = "ContentHome")
data class HomeContent(
    @PrimaryKey
    val content_id: Int,
    val title: String,
    val date: Int,
    val publisher_logo: String?,
    val avatar: String?,
    val publisher_logo_URL: String?,
    val avatar_URL: String?
)