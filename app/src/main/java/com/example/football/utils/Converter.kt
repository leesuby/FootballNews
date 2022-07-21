package com.example.football.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.example.football.data.local.database.detail.BodyDetailContent
import com.example.football.data.local.database.detail.DetailContent
import com.example.football.data.local.database.home.HomeContent
import com.example.football.data.model.Content
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object Converter {
    // convert list Homecontent to Content
    fun convert(homeContent: List<HomeContent>): MutableList<Content> {
        val listContent: MutableList<Content> = mutableListOf()
        for (content in homeContent) {
            val c = Content(
                contentId = content.contentId,
                title = content.title,
                date = content.date,
                avatarUrl = content.avatar,
                publisherLogo = content.publisherLogo,
            )
            if (!content.avatar.isNullOrBlank()) {
                try {
                    //Convert image from local to bitmap
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
                } catch (e: Throwable) {
                    if (Global.internet) {
                        //Convert image from internet to bitmap
                        if (content.avatarURL.isNullOrBlank()) {
                            c.bitmapAvatar = null
                        } else {
                            c.bitmapAvatar = loadBitmapFromUrl(content.avatarURL)
                        }
                    } else
                        c.bitmapAvatar = null


                }
            }

            if (!content.publisherLogo.isNullOrBlank()) {
                try {
                    //Convert image from local to bitmap
                    c.bitmapLogo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                MainApplication.applicationContext().contentResolver,
                                File(content.publisherLogo).toUri()
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            MainApplication.applicationContext().contentResolver,
                            File(content.publisherLogo).toUri()
                        )
                    }
                } catch (e: Throwable) {
                    if (Global.internet) {
                        //Convert image from internet to bitmap
                        if (content.publisherLogoURL.isNullOrBlank()) {
                            c.bitmapLogo = null
                        } else {
                            c.bitmapLogo = loadBitmapFromUrl(content.publisherLogoURL)
                        }
                    } else
                        c.bitmapLogo = null

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
        val listBody: MutableList<com.example.football.data.model.detail.Body> = mutableListOf()
        val detailContent = detailContent[0]
        for (body in bodyDetailContent) {
            val b = com.example.football.data.model.detail.Body(
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
            contentId = detailContent.contentId
        )
    }

    // Function to establish connection and load image
    fun loadBitmapFromUrl(string: String?): Bitmap? {
        if (string.isNullOrBlank())
            return null

        val url: URL = convertStringToURL(string)!!
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


    //Save image to external storage (if don't have cache) or cache
    fun saveImageToExternalStorage(url: String?, nameC: String, nameI: String): String? {

        //get Bitmap from URL
        val bitmap: Bitmap? = loadBitmapFromUrl(url) ?: return null

        // Get the external storage directory path
        val dirpath = Environment.getExternalStorageDirectory().absolutePath

        // Save to cache when Android O above
        val path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Global.cacheDir + Global.seperator + Global.AppName + Global.seperator + nameC
        } else {
            dirpath + Global.seperator + Global.AppName + Global.seperator + nameC
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
    private fun convertStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
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