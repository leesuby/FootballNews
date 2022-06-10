package com.example.football.utils

import java.util.*

class Helpers {
    companion object {
    public fun CalculateDistanceTime(date: Int) : String{
        val calendar: Calendar = Calendar.getInstance()
        val distanceTime = calendar.timeInMillis/1000 - date
        when{
            distanceTime > 86400 -> return "${distanceTime/86400} ngày"
            distanceTime > 3600 -> return "${distanceTime/3600} giờ"
            distanceTime > 60 -> return "${distanceTime/60} phút"
            else -> {
                return "Vừa đăng"
            }
        }
    }
}
}