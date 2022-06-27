package com.example.football.data.model

import java.io.Serializable

data class HomeBaoMoiData(
    val data: Data,
    val error_code: Int,
    val error_message: String
) : Serializable