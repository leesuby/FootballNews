package com.example.football.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class HomeBaoMoiData(
    val data: Data,
    val error_code: Int,
    val error_message: String
)