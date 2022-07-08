package com.example.football.view.customview

import android.R.attr.radius
import android.R.color
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View


class NewsCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    private var avatar : Bitmap? = null
    private lateinit var title : String
    private lateinit var logo : Bitmap
    private lateinit var time : String
    var readyToDraw  = false

    private val DEFAULT_BOARDER_STROKE = 5f
    private var removeBorder = false


    private var borderColor = 0
    private var borderWidth = 0

    fun setAvatarBitmap(bitmap: Bitmap?){
        avatar = bitmap
    }

    fun setTitle(text: String){
        title = text
    }

    fun setLogoBitmap(bitmap: Bitmap){
        logo= bitmap
    }

    fun setTime(text: String){
        time = text
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(readyToDraw) {

            val paint : Paint = Paint()
            paint.color=Color.GRAY
            paint.textSize = 30F
            canvas.drawColor(Color.WHITE)
            canvas.drawBitmap(avatar!!, null, Rect(0, 0, 200, 200), null)
            canvas.drawText("alooooooooooooooooooooooooooo",100F,100F,paint)



            super.onDraw(canvas)

        }
    }




    private fun cropBitmap(sourceBmp: Bitmap): Bitmap {
        val outputBmp: Bitmap = if (sourceBmp.width > sourceBmp.height) {
            Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.height, sourceBmp.height)
        } else if (sourceBmp.width < sourceBmp.height) {
            Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.width, sourceBmp.width)
        } else {
            sourceBmp
        }
        return outputBmp
    }

    private fun getRoundedCroppedBitmap(bitmap: Bitmap, radius: Int): Bitmap {
        val finalBitmap: Bitmap = if (bitmap.width != radius || bitmap.height != radius) {
            // Set the filter to false, because we don't need very smooth one! It's cheaper!
            Bitmap.createScaledBitmap(bitmap, radius, radius, false)
        } else {
            bitmap
        }
        val output =
            Bitmap.createBitmap(finalBitmap.width, finalBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, finalBitmap.width, finalBitmap.height)
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)

        // It doesn't matter which color!
        paint.color = Color.WHITE
        canvas.drawRoundRect(
            RectF(
                0F, 0F, finalBitmap.width.toFloat(),
                finalBitmap.height.toFloat()
            ), 15F, 15F, paint
        )


        // The second drawing should only be visible of if overlapping with the first
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(finalBitmap, rect, rect, paint)

        // Draw the border, if exist
        if (!removeBorder) {
            canvas.drawRoundRect(
                RectF(
                    0F, 0F, finalBitmap.width.toFloat(),
                    finalBitmap.height.toFloat()
                ), 15F, 15F, getBorderPaint()!!
            )
        }
        return output
    }

    private fun getBorderPaint(): Paint? {
        val borderPaint = Paint()
        if (borderColor !== 0) {
            borderPaint.color = borderColor
        } else {
            borderPaint.color = Color.WHITE
        }
        if (borderWidth !== 0) {
            borderPaint.strokeWidth = borderWidth.toFloat()
        } else {
            borderPaint.strokeWidth = DEFAULT_BOARDER_STROKE
        }
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        return borderPaint
    }




    /**
     * A method to se the color of the border of the image view
     *
     * @param colorResource The resource id of the favourite color
     */
    fun setBorderColor(colorResource: Int) {
        this.borderColor = colorResource
    }

    /**
     * A method to set the thickness of the border of the image view
     *
     * @param width The width of the border stroke in pixels
     */
    fun setBorderWidth(width: Int) {
        this.borderWidth = width
    }

    /**
     * A method to set whether show the image view with border or not
     *
     * @param removeBorder true to remove the border of the iamge view, otherwise it will have a border
     */
    private fun removeBorder(removeBorder: Boolean) {
        this.removeBorder = removeBorder
    }


}