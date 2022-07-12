package com.example.football.data.local.database.detail

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.football.data.local.database.home.HomeContent

@Entity( primaryKeys = ["content_id","related_id"],
    tableName = "ContentRelated"
)
data class RelatedContent(
    val content_id: Int,
    val related_id: Int,
    val title: String,
    val date: Int,
    val publisher_logo: String?,
    val avatar: String?,
    val publisher_logo_URL: String?,
    val avatar_URL: String?
)