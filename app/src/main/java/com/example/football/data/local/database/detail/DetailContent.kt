package com.example.football.data.local.database.detail

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ContentDetail")
data class DetailContent(
    @PrimaryKey
    val contentId: Int,
    val title: String,
    val date: Int,
    val description: String
)