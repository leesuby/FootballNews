package com.example.football.data.local.database.detail

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["contentId","bodyId"]
    , tableName = "DetailBody"
)
data class BodyDetailContent(
    val contentId: Int,
    val content: String,
    val originUrl: String?,
    val subtype: String? ="",
    val type: String,
    val bodyId: Int
)