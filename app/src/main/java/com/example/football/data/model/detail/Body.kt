package com.example.football.data.model.detail

import android.graphics.Bitmap

data class Body(
    val content: String,
    val duration: String = "",
    val height: Int = 0,
    val originUrl: String?,
    val subtype: String?,
    val type: String,
    val width: Int = 0
)