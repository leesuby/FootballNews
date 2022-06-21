package com.example.football.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.room.TypeConverter
import com.example.football.data.local.database.HomeContent
import com.example.football.data.model.Content
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class Helpers {
    companion object {
        var internet : Boolean = true

        //calculate time to show time between now and posted time of that news
        fun CalculateDistanceTime(date: Int) : String{
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

        // Function to establish connection and load image
        fun mLoad(string: String): Bitmap? {
            val url: URL = mStringToURL(string)!!
            val connection: HttpURLConnection?
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                return BitmapFactory.decodeStream(bufferedInputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        // Function to convert string to URL
        private fun mStringToURL(string: String): URL? {
            try {
                return URL(string)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            return null
        }

        // convert list homecontent to Content
        fun convert(homeContent: List<HomeContent>) : MutableList<Content>{
            var listContent : MutableList<Content> = mutableListOf()
            for (content in homeContent){
                var c = Content(content_id = content.content_id,
                    title = content.title,
                    date = content.date,
                    avatar_bitmap = content.avatar,
                    logo_bitmap = content.publisher_logo)
                    listContent.add(c)
            }
            return listContent
        }

    }

    //convert Bitmap to ByteArray
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }

    //convert ByteArray to Bitmap
    @TypeConverter
    fun toBitmap(byteArray: ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}



