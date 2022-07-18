package com.example.football.data.local.database.detail

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.football.data.local.database.home.HomeContent

@Entity( primaryKeys = ["contentId","relatedId"],
    tableName = "ContentRelated"
)
data class RelatedContent(
    val contentId: Int,
    val relatedId: Int,
    val title: String,
    val date: Int,
    val publisherLogo: String?,
    val avatar: String?,
    val publisherLogoURL: String?,
    val avatarURL: String?
)