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

class Helpers {
    companion object {
        var internet: Boolean = true
        var isOfflineMode: Boolean = false
        var isListNewsSaved: Boolean = false
        val AppName = "BongDaMoi"
        val seperator = "/"
        var serviceIsBound = false
        var cacheDir: String = ""
        var contentSave: MutableList<Content> = mutableListOf()

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

        //set Margin for View
        fun View.margin(
            left: Float? = null,
            top: Float? = null,
            right: Float? = null,
            bottom: Float? = null
        ) {
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
        fun Context.dpToPx(dp: Float): Int =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
                .toInt()

        // Function to establish connection and load image
        fun mLoad(string: String?): Bitmap? {
            if (string.isNullOrBlank())
                return null

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

        fun saveImageToExternalStorage(url: String?, nameC: String, nameI: String): String? {

            //get Bitmap from URL
            var bitmap: Bitmap? = mLoad(url) ?: return null

            // Get the external storage directory path
            val dirpath = Environment.getExternalStorageDirectory().absolutePath

            // Save to cache when Android O above
            val path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cacheDir + seperator + AppName + seperator + nameC
            } else {
                dirpath + seperator + AppName + seperator + nameC
            }

            // Create a folder to save the image
            val directory = File(path)
            directory.mkdirs()

            // Create a file for save image
            val file = File(path, "/${nameI}.png")

            try {
                // Get the file output stream
                val stream: FileOutputStream = FileOutputStream(file.absolutePath)

                // Compress the bitmap
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)

                // Flush the output stream
                stream.flush()

                // Close the output stream
                stream.close()


            } catch (e: IOException) { // Catch the exception
                e.printStackTrace()

            }

            // Return the saved image path to uri
            return file.absolutePath
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
        fun convert(homeContent: List<HomeContent>): MutableList<Content> {
            var listContent: MutableList<Content> = mutableListOf()
            for (content in homeContent) {
                var c = Content(
                    content_id = content.content_id,
                    title = content.title,
                    date = content.date,
                    avatar_url = content.avatar,
                    publisher_logo = content.publisher_logo
                )
                if (!content.avatar.isNullOrBlank()) {
                    Log.e("contentavatar", content.avatar.toString())


                    c.bitmapAvatar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                MainApplication.applicationContext().contentResolver,
                                File(content.avatar).toUri()
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            MainApplication.applicationContext().contentResolver,
                            File(content.avatar).toUri()
                        )
                    }
                }

                if (!content.publisher_logo.isNullOrBlank()) {
                    Log.e("contentavatar", content.publisher_logo.toString())


                    c.bitmapLogo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                MainApplication.applicationContext().contentResolver,
                                File(content.publisher_logo).toUri()
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            MainApplication.applicationContext().contentResolver,
                            File(content.publisher_logo).toUri()
                        )
                    }

                }
                listContent.add(c)
            }
            return listContent
        }

        // convert list Detail and Body to Content
        fun convert(
            detailContent: List<DetailContent>,
            bodyDetailContent: List<BodyDetailContent>,
        ): com.example.football.data.model.detail.Content {
            var listBody: MutableList<com.example.football.data.model.detail.Body> = mutableListOf()
            var detailContent = detailContent[0]
            for (body in bodyDetailContent) {
                var b = com.example.football.data.model.detail.Body(
                    content = body.content,
                    type = body.type,
                    subtype = body.subtype,
                    originUrl = body.originUrl
                )
                listBody.add(b)
            }

            return com.example.football.data.model.detail.Content(
                body = listBody,
                date = detailContent.date,
                title = detailContent.title,
                description = detailContent.description,
                content_id = detailContent.content_id
            )
        }

        fun checkandLoadImageGlide(url: String?, view: ImageView, context: Context) {
            if (!url.isNullOrBlank()) {
                if (internet) {
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

    //convert Bitmap to ByteArray
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray {
        val outputStream = ByteArrayOutputStream()

        if (bitmap == null)
            return outputStream.toByteArray()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    //convert ByteArray to Bitmap
    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        if (byteArray == null)
            return null
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}



