package com.example.football.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.football.R
import com.example.football.data.local.database.detail.BodyDetailContent
import com.example.football.data.local.database.detail.DetailContent
import com.example.football.data.local.database.home.HomeContent
import com.example.football.data.model.Content
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

object Helpers {
    //calculate time to show time between now and posted time of that news
    fun CalculateDistanceTime(date: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        val distanceTime = calendar.timeInMillis / 1000 - date
        return when {
            distanceTime > 604800 -> "${calendar.time}"
            distanceTime > 86400 -> "${distanceTime / 86400} ngày"
            distanceTime > 3600 -> "${distanceTime / 3600} giờ"
            distanceTime > 60 -> "${distanceTime / 60} phút"
            else -> {
                "Vừa đăng"
            }
        }
    }

    //using Glide to check and load image
    fun checkandLoadImageGlide(url: String?, view: ImageView, context: Context) {
        if (!url.isNullOrBlank()) {
            if (Global.internet) {
                Glide.with(context)
                    .load(url)
                    .apply(
                        RequestOptions
                            .bitmapTransform(RoundedCorners(20))
                            .error(R.drawable.ic_launcher_background)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    )
                    .into(view)
            } else {
                Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                    .into(view)
            }
        }
    }

}





