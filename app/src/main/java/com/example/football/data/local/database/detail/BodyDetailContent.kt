package com.example.football.data.local.database.detail

import androidx.room.Entity


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