package com.example.football.data.local.database.detail

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = DetailContent::class,
    parentColumns = arrayOf("content_id"),
    childColumns = arrayOf("content_id"),
    onDelete = ForeignKey.CASCADE
)], primaryKeys = ["content_id","body_id"]
    , tableName = "DetailBody"
)
data class BodyDetailContent(
    val content_id: Int,
    val content: String,
    val originUrl: Bitmap?,
    val subtype: String? ="",
    val type: String,
    val body_id: Int
)