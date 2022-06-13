package com.example.football.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import java.util.*

class Helpers {
    companion object {
    public fun CalculateDistanceTime(date: Int) : String{
        val calendar: Calendar = Calendar.getInstance()
        val distanceTime = calendar.timeInMillis/1000 - date
        when{
            distanceTime > 604800 -> return "${calendar.time}"
            distanceTime > 86400 -> return "${distanceTime/86400} ngày"
            distanceTime > 3600 -> return "${distanceTime/3600} giờ"
            distanceTime > 60 -> return "${distanceTime/60} phút"
            else -> {
                return "Vừa đăng"
            }
        }
    }
        //set Margin for View
        fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
            layoutParams<ViewGroup.MarginLayoutParams> {
                left?.run { leftMargin = dpToPx(this) }
                top?.run { topMargin = dpToPx(this) }
                right?.run { rightMargin = dpToPx(this) }
                bottom?.run { bottomMargin = dpToPx(this) }
            }
        }
        inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
            if (layoutParams is T) block(layoutParams as T)
        }
        fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
        fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }
}



