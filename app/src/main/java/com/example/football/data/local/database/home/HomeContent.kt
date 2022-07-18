package com.example.football.data.local.database.home

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.football.data.model.Content

@Entity(tableName = "ContentHome")
data class HomeContent(
    @PrimaryKey
    val contentId: Int,
    val title: String,
    val date: Int,
    val publisherLogo: String?,
    val avatar: String?,
    val publisherLogoURL: String?,
    val avatarURL: String?
)